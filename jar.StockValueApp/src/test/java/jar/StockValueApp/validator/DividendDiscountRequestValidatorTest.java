package jar.StockValueApp.validator;


import jar.StockValueApp.dto.DividendDiscountRequestDTO;
import jar.StockValueApp.exception.IncorrectCompaniesExpectedGrowthException;
import jar.StockValueApp.exception.MandatoryFieldsMissingException;
import jar.StockValueApp.exception.NoDividendDiscountModelFoundException;
import jar.StockValueApp.exception.ValuationDoestExistForSelectedUserException;
import jar.StockValueApp.model.DividendDiscountModel;
import jar.StockValueApp.model.User;
import jar.StockValueApp.repository.DividendDiscountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DividendDiscountRequestValidatorTest {

    @Mock
    private DividendDiscountRepository dividendDiscountRepository;

    @InjectMocks
    private DividendDiscountRequestValidator validator;

    /* Tests for validateDividendDiscountRequest */
    @Test
    void validateDividendDiscountRequest_NullRequest_ThrowsException() {
        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDividendDiscountRequest(null)
        );
        Assertions.assertEquals("Request was empty.", exception.getMessage());
    }

    @Test
    void validateDividendDiscountRequest_CompanyNameNull_ThrowsException() {
        final var requestDTO = new DividendDiscountRequestDTO(
                null,
                "ticker",
                1.1,
                1.1,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDividendDiscountRequest
                        (requestDTO)
        );
        Assertions.assertEquals("Mandatory company name field is missing.", exception.getMessage());
    }

    @Test
    void validateDividendDiscountRequest_CompanyNameIsBlank_ThrowsException() {
        final var requestDTO = new DividendDiscountRequestDTO(
                "",
                "ticker",
                1.1,
                1.1,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDividendDiscountRequest
                        (requestDTO)
        );
        Assertions.assertEquals("Mandatory company name field is empty.", exception.getMessage());
    }

    @Test
    void validateDividendDiscountRequest_CompanyTickerNull_ThrowsException() {
        final var requestDTO = new DividendDiscountRequestDTO(
                "company",
                null,
                1.1,
                1.1,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDividendDiscountRequest
                        (requestDTO)
        );

        Assertions.assertEquals("Mandatory company ticker field is missing.", exception.getMessage());
    }

    @Test
    void validateDividendDiscountRequest_CompanyTickerIsBlank_ThrowsException() {
        final var requestDTO = new DividendDiscountRequestDTO(
                "company",
                "",
                1.1,
                1.1,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDividendDiscountRequest
                        (requestDTO)
        );
        Assertions.assertEquals("Mandatory company ticker field is empty.", exception.getMessage());
    }

    @Test
    void validateDividendDiscountRequest_CurrentYearsDivIsMissing_ThrowsException() {
        final var requestDTO = new DividendDiscountRequestDTO(
                "company",
                "ticker",
                null,
                1.1,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDividendDiscountRequest
                        (requestDTO)
        );
        Assertions.assertEquals("Mandatory currentYearsDiv field is missing.", exception.getMessage());
    }

    @Test
    void validateDividendDiscountRequest_WaccFieldIsMissing_ThrowsException() {
        final var requestDTO = new DividendDiscountRequestDTO(
                "company",
                "ticker",
                1.1,
                null,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDividendDiscountRequest
                        (requestDTO)
        );
        Assertions.assertEquals("Mandatory wacc field is missing.", exception.getMessage());
    }

    @Test
    void validateDividendDiscountRequest_ExpectedGrowthRateIsMissing_ThrowsException() {
        final var requestDTO = new DividendDiscountRequestDTO(
                "company",
                "ticker",
                1.1,
                1.1,
                null
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDividendDiscountRequest
                        (requestDTO)
        );
        Assertions.assertEquals("Mandatory expectedGrowthRate field is missing.", exception.getMessage());

    }

    /* Tests for validateDividendDiscountList */
    @Test
    void validateDividendDiscountList_EmptyList_ThrowsException() {
        final List<DividendDiscountModel> valuations = new ArrayList<>();

        NoDividendDiscountModelFoundException exception = assertThrows(
                NoDividendDiscountModelFoundException.class,
                () -> validator.validateDividendDiscountList(valuations)
        );

        Assertions.assertEquals("No dividend discount valuations found.", exception.getMessage());
    }

    @Test
    void validateDividendDiscountList_PopulatedList_DoesNotThrowException() {
        final List<DividendDiscountModel> valuations = new ArrayList<>();
        valuations.add(new DividendDiscountModel());
        valuations.add(new DividendDiscountModel());
        valuations.add(new DividendDiscountModel());

        assertDoesNotThrow(() -> validator.validateDividendDiscountList(valuations));
    }

    /* Tests for validateDividendDiscountById */
    @Test
    void validateDividendDiscountById_DoesNotExist_ThrowsException() {
        final var valuationId = 1L;
        when(dividendDiscountRepository.existsById(valuationId)).thenReturn(false);

        final var exception = assertThrows(
                NoDividendDiscountModelFoundException.class,
                () -> validator.validateDividendDiscountById(valuationId)
        );

        Assertions.assertEquals("Dividend discount valuation with id number "
                + valuationId
                + " not found.", exception.getMessage());
    }

    @Test
    void validateDividendDiscountById_DoesExist_DoesNotThrowException() {
        final var valuationId = 1L;
        when(dividendDiscountRepository.existsById(valuationId)).thenReturn(true);

        assertDoesNotThrow(() -> validator.validateDividendDiscountById(valuationId));
    }

    /* Tests for validateExpectedGrowthRateInput */
    @Test
    void validateExpectedGrowthRateInput_ExpectedGrowthHigherThenWacc_ThrowsException() {
        final var wacc = 9.0;
        final var expectedGrowth = 9.5;

        final var exception = assertThrows(
                IncorrectCompaniesExpectedGrowthException.class,
                () -> validator.validateExpectedGrowthRateInput(wacc, expectedGrowth)
        );

        Assertions.assertEquals("Dividend discount model isn't suitable" +
                        " to calculate intrinsic value if expected growth" +
                        " rate is higher or equal to the weighted average cost of capital.", exception.getMessage());
    }

    @Test
    void validateExpectedGrowthRateInput_ExpectedGrowthEqualToWacc_ThrowsException() {
        final var wacc = 9.0;
        final var expectedGrowth = 9.0;

        final var exception = assertThrows(
                IncorrectCompaniesExpectedGrowthException.class,
                () -> validator.validateExpectedGrowthRateInput(wacc, expectedGrowth)
        );

        Assertions.assertEquals("Dividend discount model isn't suitable to calculate" +
                        " intrinsic value if expected growth rate is higher or" +
                        " equal to the weighted average cost of capital.", exception.getMessage());
    }

    @Test
    void validateExpectedGrowthRateInput_ExpectedGrowthLowerThanWacc_DoesNotThrowException() {
        final var wacc = 9.0;
        final var expectedGrowth = 7.5;

        assertDoesNotThrow(() -> validator.validateExpectedGrowthRateInput(wacc, expectedGrowth));
    }

    /* Tests for validateDividendDiscountModelForUser */
    @Test
    void validateDividendDiscountModelForUser_ExistsAndBelongsToUser_NoExceptionThrown() {
        final var valuationId = 1L;
        final var userId = 1L;
        final var user = new User(
                1L,
                "Andrius",
                "password",
                "myEmail@gmail.com",
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        final var dividendDiscountModel = new DividendDiscountModel(
                1L,
                "Apple",
                "AAPL",
                1.1,
                1.1,
                1.1,
                1.5,
                5.5,
                null,
                user
        );

        when(dividendDiscountRepository.findById(valuationId))
                .thenReturn(Optional.of(dividendDiscountModel));

        assertDoesNotThrow(() -> validator.validateDividendDiscountModelForUser(valuationId, userId));
    }

    @Test
    void validateDividendDiscountModelForUser_ValuationDoesNotExist_ThrowsException() {
        final var valuationId = 1L;
        final var userId = 1L;
        when(dividendDiscountRepository.findById(valuationId))
                .thenReturn(Optional.empty());

        final var exception = assertThrows(
                ValuationDoestExistForSelectedUserException.class,
                () -> validator.validateDividendDiscountModelForUser(valuationId, userId)
        );

        Assertions.assertEquals("Valuation does not exist for this user", exception.getMessage());
    }

    @Test
    void validateDividendDiscountModelForUser_ValuationExistsButBelongsToAnotherUser_ThrowsException() {
        final var valuationId = 1L;
        final var userId = 2L;
        final var user = new User(
                1L,
                "Andrius",
                "password",
                "myEmail@gmail.com",
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        final var dividendDiscountModel = new DividendDiscountModel(
                1L,
                "Apple",
                "AAPL",
                1.1,
                1.1,
                1.1,
                1.5,
                5.5,
                null,
                user
        );

        when(dividendDiscountRepository.findById(valuationId))
                .thenReturn(Optional.of(dividendDiscountModel));

        final var exception = assertThrows(
                ValuationDoestExistForSelectedUserException.class,
                () -> validator.validateDividendDiscountModelForUser(valuationId, userId)
        );

        Assertions.assertEquals("Valuation does not exist for this user", exception.getMessage());
    }
}