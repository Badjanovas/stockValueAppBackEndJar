package jar.StockValueApp.validator;


import jar.StockValueApp.dto.DcfModelRequestDTO;
import jar.StockValueApp.exception.MandatoryFieldsMissingException;
import jar.StockValueApp.exception.NoDcfValuationsFoundException;
import jar.StockValueApp.exception.ValuationDoestExistForSelectedUserException;
import jar.StockValueApp.model.DcfModel;
import jar.StockValueApp.model.User;
import jar.StockValueApp.repository.DcfModelRepository;
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
class DcfRequestValidatorTest {

    @Mock
    private DcfModelRepository dcfModelRepository;

    @InjectMocks
    private DcfRequestValidator validator;

    /* Tests for validateDcfModelRequest */
    @Test
    void validateDcfModelRequest_NullRequest_ThrowsException() {
        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDcfModelRequest(null)
        );
        Assertions.assertEquals("Request was empty.", exception.getMessage());
    }

    @Test
    void validateDcfModelRequest_CompanyNameNull_ThrowsException() {
        final var requestDTO = new DcfModelRequestDTO(
                null,
                "ticker",
                1.1,
                1.1,
                1.1,
                1.1,
                1.1,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDcfModelRequest(requestDTO)
        );
        Assertions.assertEquals("Mandatory company name field is missing.", exception.getMessage());
    }

    @Test
    void validateDcfModelRequest_CompanyNameIsBlank_ThrowsException() {
        final var requestDTO = new DcfModelRequestDTO(
                "",
                "ticker",
                1.1,
                1.1,
                1.1,
                1.1,
                1.1,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDcfModelRequest(requestDTO)
        );
        Assertions.assertEquals("Mandatory company name field is empty.", exception.getMessage());
    }

    @Test
    void validateDcfModelRequest_CompanyTickerIsNull_ThrowsException() {
        final var requestDTO = new DcfModelRequestDTO(
                "company",
                null,
                1.1,
                1.1,
                1.1,
                1.1,
                1.1,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDcfModelRequest(requestDTO)
        );
        Assertions.assertEquals("Mandatory company ticker field is missing.", exception.getMessage());
    }

    @Test
    void validateDcfModelRequest_CompanyTickerIsBlank_ThrowsException() {
        final var requestDTO = new DcfModelRequestDTO(
                "company",
                "",
                1.1,
                1.1,
                1.1,
                1.1,
                1.1,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDcfModelRequest(requestDTO)
        );
        Assertions.assertEquals("Mandatory company ticker field is empty.", exception.getMessage());
    }

    @Test
    void validateDcfModelRequest_SumOfFCFIsNull_ThrowsException() {
        final var requestDto = new DcfModelRequestDTO(
                "company",
                "ticker",
                null,
                1.1,
                1.1,
                1.1,
                1.1,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDcfModelRequest(requestDto)
        );
        Assertions.assertEquals("Mandatory sumOfFCF field is missing.", exception.getMessage());
    }

    @Test
    void validateDcfModelRequest_CashAndCashEquivalentsIsNull_ThrowsException() {
        final var requestDTO = new DcfModelRequestDTO(
                "company",
                "ticker",
                1.1,
                null,
                1.1,
                1.1,
                1.1,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDcfModelRequest(requestDTO)
        );
        Assertions.assertEquals("Mandatory cashAndCashEquivalents field is missing.", exception.getMessage());
    }

    @Test
    void validateDcfModelRequest_TotalDebtIsNull_ThrowsException() {
        final var requestDTO = new DcfModelRequestDTO(
                "company",
                "ticker",
                1.1,
                1.1,
                null,
                1.1,
                1.1,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDcfModelRequest(requestDTO)
        );
        Assertions.assertEquals("Mandatory totalDebt field is missing.", exception.getMessage());
    }

    @Test
    void validateDcfModelRequest_SharesOutstandingIsNull_ThrowsException() {
        final var requestDTO = new DcfModelRequestDTO(
                "company",
                "ticker",
                1.1,
                1.1,
                1.1,
                null,
                1.1,
                1.1
        );

        final var exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateDcfModelRequest(requestDTO)
        );
        Assertions.assertEquals("Mandatory sharesOutstanding field is missing.", exception.getMessage());
    }

    /* Tests for validateDcfModelList */
    @Test
    void validateDcfModelList_EmptyList_ThrowsException() {
        final List<DcfModel> valuations = new ArrayList<>();

        final var exception = assertThrows(
                NoDcfValuationsFoundException.class,
                () -> validator.validateDcfModelList(valuations)
        );

        Assertions.assertEquals("No discounted cash flow valuations found.", exception.getMessage());
    }

    @Test
    void validateDcfModelList_PopulatedList_DoesNotThrowException() {
        final List<DcfModel> valuations = new ArrayList<>();
        valuations.add(new DcfModel());
        valuations.add(new DcfModel());
        valuations.add(new DcfModel());

        assertDoesNotThrow(() -> validator.validateDcfModelList(valuations));
    }

    /* Tests for validateDcfModelById */
    @Test
    void validateDcfModelById_DoesNotExist_ThrowsException() {
        final var valuationId = 1L;

        final var exception = assertThrows(
                NoDcfValuationsFoundException.class,
                () -> validator.validateDcfModelById(valuationId)
        );

        Assertions.assertEquals("Discounted cash flow valuation with id number " + valuationId + " not found.", exception.getMessage());
    }

    @Test
    void validateDcfModelById_DoesExist_DoesNotThrowException() {
        when(dcfModelRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> validator.validateDcfModelById(1L));
    }

    @Test
    void validateDcfModelForUser_ExistsAndBelongsToUser_NoExceptionThrown() {
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

        final var dcfModel = new DcfModel(
                "Apple",
                "AAPL",
                10.1,
                2.1,
                3.1,
                1.1,
                user
        );

        when(dcfModelRepository.findById(valuationId)).thenReturn(Optional.of(dcfModel));

        assertDoesNotThrow(() -> validator.validateDcfModelForUser(valuationId, userId));
    }

    @Test
    void validateDcfModelForUser_ValuationDoesNotExist_ThrowsException() {
        final var valuationId = 1L;
        final var userId = 1L;

        when(dcfModelRepository.findById(valuationId))
                .thenReturn(Optional.empty());

        final var exception = assertThrows(
                ValuationDoestExistForSelectedUserException.class,
                () -> validator.validateDcfModelForUser(valuationId, userId)
        );

        Assertions.assertEquals("Valuation does not exist for this user", exception.getMessage());
    }

    @Test
    void validateDcfModelForUser_ValuationExistsButBelongsToAnotherUser_ThrowsException() {
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

        final var dcfModel = new DcfModel(
                "Apple",
                "AAPL",
                10.1,
                2.1,
                3.1,
                1.1,
                user
        );

        when(dcfModelRepository.findById(valuationId))
                .thenReturn(Optional.of(dcfModel));

        final var exception = assertThrows(
                ValuationDoestExistForSelectedUserException.class,
                () -> validator.validateDcfModelForUser(valuationId, userId)
        );

        Assertions.assertEquals("Valuation does not exist for this user", exception.getMessage());
    }
}