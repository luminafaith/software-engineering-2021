package com.example.simplecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private enum Operator {none, add, minus, multiply, divide, equals}
    private double data1=0, data2=0;
    private Operator optr = Operator.none;
    private boolean hadDot = false;
    private boolean requireCleaning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickNumericalButton(View view) {
        // Getting ID of pressed Button
        int pressID = view.getId();

        // Getting text object where we display the current number value
        TextView curText = findViewById(R.id.resultEdit);

        // Cleaning
        if (optr == Operator.equals) {
            optr = Operator.none;
            curText.setText("");
        }
        if (requireCleaning) {
            requireCleaning = false;
            curText.setText("");
        }

        //Figuring out which button was pressed and updating the represented text field object
        switch (pressID) {
            case R.id.btn_00:
                curText.setText(curText.getText() + "0");
                break;
            case R.id.btn_01:
                curText.setText(curText.getText() + "1");
                break;
            case R.id.btn_02:
                curText.setText(curText.getText() + "2");
                break;
            case R.id.btn_03:
                curText.setText(curText.getText() + "3");
                break;
            case R.id.btn_04:
                curText.setText(curText.getText() + "4");
                break;
            case R.id.btn_05:
                curText.setText(curText.getText() + "5");
                break;
            case R.id.btn_06:
                curText.setText(curText.getText() + "6");
                break;
            case R.id.btn_07:
                curText.setText(curText.getText() + "7");
                break;
            case R.id.btn_08:
                curText.setText(curText.getText() + "8");
                break;
            case R.id.btn_09:
                curText.setText(curText.getText() + "9");
                break;
            case R.id.btnDot:
                if(!hadDot) {
                    curText.setText(curText.getText() + ".");
                    hadDot = true;
                }
                break;
            default:
                curText.setText("ERROR");
                Log.d("error", "Error: Unknown Button pressed!");
                break;
        }
    }

    public void onClickFunctionButton(View view) {
        int pressID = view.getId();
        TextView curText = findViewById(R.id.resultEdit);

        // CE clears all
        if (pressID == R.id.btnClear) {
            optr = Operator.none;
            curText.setText("");
            data1 = 0;
            data2 = 0;
            hadDot = false;
            requireCleaning = false;
            return;
        }

        String dataText = curText.getText().toString();
        double numberValue = dataText.length()>0 ? Double.parseDouble(dataText):0;

        if (optr == Operator.none) {
            data1 = numberValue;
            requireCleaning = true;
            switch (pressID) {
                case R.id.btnResult:
                    optr = Operator.equals;
                    data1 = 0;
                    break;
                case R.id.btnAdd:
                    optr = Operator.add;
                    break;
                case R.id.btnMinus:
                    optr = Operator.minus;
                    break;
                case R.id.btnMultiply:
                    optr = Operator.multiply;
                    break;
                case R.id.btnDivide:
                    optr = Operator.divide;
                    break;
                case R.id.btnClear:
                    optr = Operator.none;
                    break;
            }
        }
        else {
            double result = 0;
            data2 = numberValue;

            switch (optr) {
                case equals:
                    break;
                case none:
                    break;
                case add:
                    result = data1 + data2;
                    break;
                case minus:
                    result = data1 - data2;
                    break;
                case multiply:
                    result = data1 * data2;
                    break;
                case divide:
                    result = data1 / data2;
                    break;
            }
            data1 = result;
            optr = Operator.none;
            if ((result - (int)result) != 0) {
                curText.setText(String.valueOf(result));
            }
            else {
                curText.setText(String.valueOf((int)result));
            }
        }
    }
}