package com.sudoku.sudokurestv2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sudoku.Sudoku;

@RestController
@SpringBootApplication
public class SudokuRestV2Application {
    private String solved;
    @PostMapping("/insertAndSolveSudoku")
    public ResponseEntity<String> solve(@RequestBody int[][] array){
        Sudoku sudoku = new Sudoku(array);
        try{
            sudoku.convert2DArrayToBlocks();
            solved = sudoku.toJSON();
            return new ResponseEntity<>("Solved...", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Failed to solve...", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/solvedSudoku")
    public ResponseEntity<String> solved() throws JSONException {
        if(solved!=null){
            JSONArray outerArr = new JSONArray(solved);
            JSONArray finalArr = new JSONArray();
            for(int i=0; i<outerArr.length(); i++){
                StringBuilder stringBuilder = new StringBuilder();
                JSONArray innerArr = new JSONArray(String.valueOf(outerArr.get(i)));
                finalArr.put(convertJSONArrayValuesToString(innerArr, stringBuilder));
            }
            return new ResponseEntity<>(finalArr.toString(5), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("No solved Sudoku available...", HttpStatus.EXPECTATION_FAILED);
        }
    }
    private StringBuilder convertJSONArrayValuesToString(JSONArray innerArr, StringBuilder stringBuilder) throws JSONException {
        for(int j=0;  j<innerArr.length(); j++){
            JSONObject obj = new JSONObject(String.valueOf(innerArr.get(j)));
            String value = obj.getString("value");
            stringBuilder.append(value);
        }
        return stringBuilder;
    }

    public static void main(String[] args) {
        SpringApplication.run(SudokuRestV2Application.class, args);
    }

}
