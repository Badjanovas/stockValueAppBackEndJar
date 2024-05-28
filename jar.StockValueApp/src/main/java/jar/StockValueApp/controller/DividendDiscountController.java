package jar.StockValueApp.controller;


import jar.StockValueApp.dto.DividendDiscountRequestDTO;
import jar.StockValueApp.exception.*;
import jar.StockValueApp.service.DividendDiscountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dividendDiscount")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://stockvalueapp.s3-website.eu-west-2.amazonaws.com"
})
public class DividendDiscountController {

    private final DividendDiscountService dividendDiscountService;

    @GetMapping("/")
    public ResponseEntity<?> findAllDividendDiscountValuations() throws NoDividendDiscountModelFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(dividendDiscountService.getAllDividendDiscountValuations());
    }

    @GetMapping("/ticker/{ticker}/{userId}")
    public ResponseEntity<?> findByTicker(@PathVariable final String ticker, @PathVariable final Long userId)
            throws NoDividendDiscountModelFoundException, NotValidIdException, NoUsersFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                dividendDiscountService.getDividendDiscountValuationsByTicker(ticker, userId)
        );
    }

    @GetMapping("/companyName/{companyName}/{userId}")
    public ResponseEntity<?> findByCompanyName(@PathVariable final String companyName, @PathVariable final Long userId)
            throws NoDividendDiscountModelFoundException, NotValidIdException, NoUsersFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                dividendDiscountService.getDividendDiscountValuationsByCompanyName(companyName, userId)
        );
    }

    @GetMapping("/date/{startDate}/{endDate}/{userId}")
    public ResponseEntity<?> findByDate(
            @PathVariable("startDate") final LocalDate startDate,
            @PathVariable final LocalDate endDate,
            @PathVariable final Long userId
    ) throws NoDividendDiscountModelFoundException, NotValidIdException, NoUsersFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                dividendDiscountService.getDividendDiscountValuationsByDate(startDate, endDate, userId)
        );
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> addDividendDiscountValuation(
            @RequestBody final DividendDiscountRequestDTO dividendDiscountRequestDTO,
            @PathVariable final Long userId
    ) throws
            MandatoryFieldsMissingException,
            NotValidIdException,
            NoUsersFoundException,
            IncorrectCompaniesExpectedGrowthException {
        return ResponseEntity.status(HttpStatus.OK).body(
                dividendDiscountService.addDividendDiscountValuation(dividendDiscountRequestDTO, userId)
        );
    }

    @DeleteMapping("/{valuationId}/{userId}")
    public ResponseEntity<?> deleteDividendDiscountById(
            @PathVariable final  Long valuationId,
            @PathVariable final Long userId
    ) throws NoDividendDiscountModelFoundException, NotValidIdException, ValuationDoestExistForSelectedUserException {
        dividendDiscountService.deleteDividendDiscountValuationById(valuationId, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Dividend discount valuation  with id number " + valuationId + " was deleted from DB successfully.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
