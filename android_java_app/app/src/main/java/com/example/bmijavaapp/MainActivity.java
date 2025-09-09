package com.example.bmijavaapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    RadioGroup heightUnitGroup, weightUnitGroup;
    EditText heightCm, heightFt, heightIn, weightInput;
    TextView resultText;
    Button calcBtn, resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heightUnitGroup = findViewById(R.id.heightUnitGroup);
        weightUnitGroup = findViewById(R.id.weightUnitGroup);
        heightCm = findViewById(R.id.heightCm);
        heightFt = findViewById(R.id.heightFt);
        heightIn = findViewById(R.id.heightIn);
        weightInput = findViewById(R.id.weightInput);
        resultText = findViewById(R.id.resultText);
        calcBtn = findViewById(R.id.calcBtn);
        resetBtn = findViewById(R.id.resetBtn);

        heightUnitGroup.setOnCheckedChangeListener((g, id) -> toggleHeightInputs());
        toggleHeightInputs();

        calcBtn.setOnClickListener(v -> calculate());
        resetBtn.setOnClickListener(v -> reset());
    }

    void toggleHeightInputs() {
        int selected = heightUnitGroup.getCheckedRadioButtonId();
        boolean useCm = selected == R.id.unitCm;
        heightCm.setVisibility(useCm ? View.VISIBLE : View.GONE);
        heightFt.setVisibility(useCm ? View.GONE : View.VISIBLE);
        heightIn.setVisibility(useCm ? View.GONE : View.VISIBLE);
    }

    void calculate() {
        try {
            double hMeters = 0;
            int selected = heightUnitGroup.getCheckedRadioButtonId();
            if (selected == R.id.unitCm) {
                String cmStr = heightCm.getText().toString().trim();
                if (TextUtils.isEmpty(cmStr)) { toast("Enter height in cm"); return; }
                double cm = Double.parseDouble(cmStr);
                if (cm <= 0) { toast("Height must be positive"); return; }
                hMeters = cm / 100.0;
            } else {
                String ftStr = heightFt.getText().toString().trim();
                String inStr = heightIn.getText().toString().trim();
                if (TextUtils.isEmpty(ftStr)) ftStr = "0";
                if (TextUtils.isEmpty(inStr)) inStr = "0";
                double ft = Double.parseDouble(ftStr);
                double inch = Double.parseDouble(inStr);
                double totalIn = ft * 12 + inch;
                if (totalIn <= 0) { toast("Height must be positive"); return; }
                hMeters = totalIn * 0.0254;
            }

            String wStr = weightInput.getText().toString().trim();
            if (TextUtils.isEmpty(wStr)) { toast("Enter weight"); return; }
            double w = Double.parseDouble(wStr);
            boolean kg = weightUnitGroup.getCheckedRadioButtonId() == R.id.unitKg;
            if (!kg) w = w * 0.45359237;
            if (w <= 0) { toast("Weight must be positive"); return; }

            double bmi = w / (hMeters * hMeters);
            double rounded = Math.round(bmi * 10.0) / 10.0;
            String cat = category(rounded);
            resultText.setText("BMI: " + rounded + "\nCategory: " + cat);
        } catch (Exception e) {
            toast("Invalid input");
        }
    }

    String category(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }

    void reset() {
        heightCm.setText("");
        heightFt.setText("");
        heightIn.setText("");
        weightInput.setText("");
        resultText.setText("");
        heightUnitGroup.check(R.id.unitCm);
        weightUnitGroup.check(R.id.unitKg);
        toggleHeightInputs();
    }

    void toast(String m) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }
}
