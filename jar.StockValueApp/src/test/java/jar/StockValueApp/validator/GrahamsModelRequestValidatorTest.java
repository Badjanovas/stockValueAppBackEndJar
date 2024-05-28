package jar.StockValueApp.validator;


import jar.StockValueApp.dto.GrahamsRequestDTO;
import jar.StockValueApp.exception.MandatoryFieldsMissingException;
import jar.StockValueApp.exception.NoGrahamsModelFoundException;
import jar.StockValueApp.exception.ValuationDoestExistForSelectedUserException;
import jar.StockValueApp.model.GrahamsModel;
import jar.StockValueApp.model.User;
import jar.StockValueApp.repository.GrahamsModelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrahamsModelRequestValidatorTest {

    @Mock
    private GrahamsModelRepository grahamsModelRepository;

    @InjectMocks
    private GrahamsModelRequestValidator validator;

    /* Tests for validateGrahamsModelRequest */
    @Test
    void validateGrahamsRequest_nullRequest_ThrowsException() {
        MandatoryFieldsMissingException exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateGrahamsModelRequest(null)
        );
        Assertions.assertEquals("Request was empty.", exception.getMessage());
    }

    @Test
    void validateGrahamsRequest_CompanyNameNull_ThrowsException() {
        var requestDTO = new GrahamsRequestDTO(
                null,
                "ticker",
                1.1,
                1.1,
                1.1
        );
        MandatoryFieldsMissingException exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateGrahamsModelRequest
                        (requestDTO)
        );
        Assertions.assertEquals("Mandatory company name field is missing.", exception.getMessage());
    }

    @Test
    void validateGrahamsRequest_CompanyNameIsBlank_ThrowsException() {
        var requestDTO = new GrahamsRequestDTO(
                "",
                "ticker",
                1.1,
                1.1,
                1.1
        );
        MandatoryFieldsMissingException exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateGrahamsModelRequest
                        (requestDTO)
        );
        Assertions.assertEquals("Mandatory company name field is empty.", exception.getMessage());
    }

    @Test
    void validateGrahamsRequest_CompanyTickerIsNull_ThrowsException() {
        var requestDTO = new GrahamsRequestDTO(
                "company",
                null,
                1.1,
                1.1,
                1.1
        );
        MandatoryFieldsMissingException exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateGrahamsModelRequest
                        (requestDTO)
        );
        Assertions.assertEquals("Mandatory company ticker field is missing.", exception.getMessage());
    }

    @Test
    void validateGrahamsRequest_CompanyTickerIsBlank_ThrowsException() {
        var requestDTO = new GrahamsRequestDTO(
                "company",
                "",
                1.1,
                1.1,
                1.1
        );
        MandatoryFieldsMissingException exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateGrahamsModelRequest
                        (requestDTO)
        );
        Assertions.assertEquals("Mandatory company ticker field is empty.", exception.getMessage());
    }

    @Test
    void validateGrahamsRequest_EpsIsNull_ThrowsException() {
        var requestDTO = new GrahamsRequestDTO(
                "company",
                "ticker",
                null,
                1.1,
                1.1
        );
        MandatoryFieldsMissingException exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateGrahamsModelRequest
                        (requestDTO)
        );
        Assertions.assertEquals("Mandatory eps field is empty.", exception.getMessage());
    }

    @Test
    void validateGrahamsRequest_GrowthRateIsNull_ThrowsException() {
        var requestDTO = new GrahamsRequestDTO(
                "company",
                "ticker",
                1.1,
                null,
                1.1
        );
        MandatoryFieldsMissingException exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateGrahamsModelRequest
                        (requestDTO)
        );
        Assertions.assertEquals("Mandatory growth rate field is empty.", exception.getMessage());
    }

    @Test
    void validateGrahamsRequest_CurrentYieldOfBondsIsNull_ThrowsException() {
        var requestDTO = new GrahamsRequestDTO(
                "company",
                "ticker",
                1.1,
                1.1,
                null
        );
        MandatoryFieldsMissingException exception = assertThrows(
                MandatoryFieldsMissingException.class,
                () -> validator.validateGrahamsModelRequest
                        (requestDTO)
        );
        Assertions.assertEquals("Mandatory current yield of bonds field is empty.", exception.getMessage());
    }

    /* Tests for validateGrahamsModelById */
    @Test
    void validateGrahamsModelById_GrahamsValuationNotFound_ThrowsException() {
        final Long valuationId = 1L;
        when(grahamsModelRepository.existsById(valuationId)).thenReturn(false);

        NoGrahamsModelFoundException exception = assertThrows(
                NoGrahamsModelFoundException.class,
                () -> validator.validateGrahamsModelById(valuationId)
        );
        Assertions.assertEquals("Grahams valuation with id number " + valuationId + " not found.", exception.getMessage());
    }

    @Test
    void validateGrahamsModelById_GrahamsValuationExist_doesNotThrowException() {
        final Long valuationId = 1L;
        when(grahamsModelRepository.existsById(valuationId)).thenReturn(true);

        assertDoesNotThrow(() -> validator.validateGrahamsModelById(valuationId));
    }

    /* Tests for validateGrahamsModelList */
    @Test
    void validateGrahamsModelList_EmptyList_ThrowsException() {
        final List<GrahamsModel> grahamsValuations = new ArrayList<>();
        NoGrahamsModelFoundException exception = assertThrows(
                NoGrahamsModelFoundException.class,
                () -> validator.validateGrahamsModelList(grahamsValuations)
        );

        Assertions.assertEquals("No Graham valuations found.", exception.getMessage());
    }

    @Test
    void validateGrahamsModelList_PopulatedList_DoesNotThrowException() {
        final List<GrahamsModel> grahamsValuations = new ArrayList<>();
        grahamsValuations.add(new GrahamsModel("Apple", "AAPL", 11.1, 1.1, 1.1, null));
        grahamsValuations.add(new GrahamsModel("Alphabet", "GOOGL", 11.1, 1.1, 1.1, null));
        grahamsValuations.add(new GrahamsModel("Apple", "AAPL", 10.1, 2.1, 3.1, null));

        assertDoesNotThrow(() -> validator.validateGrahamsModelList(grahamsValuations));
    }

    /* Tests for validateGrahamsModelForUser*/
    @Test
    void validateGrahamsModelForUser_ExistsAndBelongsToUser_NoExceptionThrown() {
        final Long valuationId = 1L;
        final Long userId = 1L;
        var user = new User(
                1L,
                "Andrius",
                "password",
                "myEmail@gmail.com",
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        var grahamsModel = new GrahamsModel(
                "Apple",
                "AAPL",
                10.1,
                2.1,
                3.1,
                user
        );
        when(grahamsModelRepository.findById(valuationId)).thenReturn(Optional.of(grahamsModel));

        assertDoesNotThrow(() -> validator.validateGrahamsModelForUser(valuationId, userId));
    }

    @Test
    void validateGrahamsModelForUser_ValuationDoesNotExist_ThrowsException() {
        final Long valuationId = 1L;
        final Long userId = 1L;

        when(grahamsModelRepository.findById(valuationId))
                .thenReturn(Optional.empty());

        ValuationDoestExistForSelectedUserException exception = assertThrows(
                ValuationDoestExistForSelectedUserException.class,
                () -> validator.validateGrahamsModelForUser(valuationId, userId)
        );

        assertEquals("Valuation does not exist for this user", exception.getMessage());
    }

    @Test
    void validateGrahamsModelForUser_ValuationExistsButBelongsToAnotherUser_ThrowsException() {
        final Long valuationId = 1L;
        final Long userId = 2L;
        var user = new User(
                1L,
                "Andrius",
                "password",
                "myEmail@gmail.com",
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        var grahamsModel = new GrahamsModel(
                "Apple",
                "AAPL",
                10.1,
                2.1,
                3.1,
                user
        );

        when(grahamsModelRepository.findById(valuationId))
                .thenReturn(Optional.of(grahamsModel));

        ValuationDoestExistForSelectedUserException exception = assertThrows(
                ValuationDoestExistForSelectedUserException.class,
                () -> validator.validateGrahamsModelForUser(valuationId, userId)
        );

        assertEquals("Valuation does not exist for this user", exception.getMessage());
    }


}