package com.example.APi_Rest_file.controller;


import com.example.APi_Rest_file.service.MainService;
import com.example.APi_Rest_file.util.RequestValidator;
import dto.Request;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.APi_Rest_file.util.MainUtil.fillInTheData;


@Tag(name = "service")
@ApiResponses({@ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "200", description = "OK")})
@RestController
@RequiredArgsConstructor
public class MainController {

    private static final String MAX_VALUE = "max_value";
    private static final String MIN_VALUE = "min_value";
    private static final String MEDIAN = "median";
    private static final String AVERAGE = "average";
    private static final String INCREASE_SEQUENCE = "increase_sequence";
    private static final String DECREASE_SEQUENCE = "decrease_sequence";

    private final RequestValidator requestValidator;

    private final MainService mainService;

    @Operation(summary = "Getting max value from file with data.")
    @Parameter(description = "Request with file path")
    @GetMapping("/get_max_value")
    public ResponseEntity<Object> getMaxValue(@RequestBody @Valid Request request, BindingResult bindingResult) {
        Map<String, Object> body = new HashMap<>();
        checkRequest(request, bindingResult);

        long hashSumFile = getHashSumFile(request.getFilePath());

        Long maxValue = mainService.getMaxValue(request, hashSumFile);
        body.put(MAX_VALUE, maxValue);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    private long getHashSumFile(String filePath) {
    }

    @Operation(summary = "Getting min value from file with data.")
    @Parameter(description = "Request with file path")
    @GetMapping("/get_min_value")
    public ResponseEntity<Object> getMinValue(@RequestBody @Valid Request request, BindingResult bindingResult) {
        Map<String, Object> body = new HashMap<>();
        checkRequest(request, bindingResult);

        long hashSumFile = getHashSumFile(request.getFilePath());

        Long minValue = mainService.getMinValue(request, hashSumFile);
        body.put(MIN_VALUE, minValue);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Operation(summary = "Getting median from file with data.")
    @Parameter(description = "Request with file path")
    @GetMapping("/get_median")
    public ResponseEntity<Object> getMedian(@RequestBody @Valid Request request, BindingResult bindingResult) {
        Map<String, Object> body = new HashMap<>();
        checkRequest(request, bindingResult);

        long hashSumFile = getHashSumFile(request.getFilePath());

        Double median = mainService.getMedian(request, hashSumFile);
        body.put(MEDIAN, median);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Operation(summary = "Getting average value from file with data.")
    @Parameter(description = "Request with file path")
    @GetMapping("/get_average")
    public ResponseEntity<Object> getAverage(@RequestBody @Valid Request request, BindingResult bindingResult) {
        Map<String, Object> body = new HashMap<>();
        checkRequest(request, bindingResult);
        long hashSumFile = getHashSumFile(request.getFilePath());

        Double average = mainService.getAverage(request, hashSumFile);
        body.put(AVERAGE, average);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Operation(summary = "Getting longest ascending sequences of numbers from file with data.")
    @Parameter(description = "Request with file path")
    @GetMapping("/get_increase_seq")
    public ResponseEntity<Object> getIncreaseSeq(@RequestBody @Valid Request request,
                                                 BindingResult bindingResult) {
        checkRequest(request, bindingResult);
        long hashSumFile = getHashSumFile(request.getFilePath());

        List<List<Long>> increaseSeq = mainService.getIncreaseSeq(request, hashSumFile);
        Map<String, Object> body = fillInTheData(INCREASE_SEQUENCE, increaseSeq);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Operation(summary = "Getting longest descending sequences of numbers from file with data.")
    @Parameter(description = "Request with file path")
    @GetMapping("/get_decrease_seq")
    public ResponseEntity<Object> getDecreaseSeq(@RequestBody @Valid Request request,
                                                 BindingResult bindingResult) {
        checkRequest(request, bindingResult);
        long hashSumFile = getHashSumFile(request.getFilePath());

        List<List<Long>> decreaseSeq = mainService.getDecreaseSeq(request, hashSumFile);
        Map<String, Object> body = fillInTheData(DECREASE_SEQUENCE, decreaseSeq);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    private void checkRequest(Request request, BindingResult bindingResult) {
        requestValidator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> allErrors = bindingResult.getFieldErrors();
            for (FieldError error : allErrors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage() == null ? error.getCode() : error.getDefaultMessage())
                        .append(";");
            }

            throw new RequestException(errorMsg.toString());
        }

    }
}

