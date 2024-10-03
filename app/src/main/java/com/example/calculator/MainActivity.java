package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvExpression, tvResult;
    MaterialButton btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9;
    MaterialButton btn_dot, btn_plus, btn_minus, btn_divide, btn_multiply;
    MaterialButton btn_C, btn_bracket1, btn_bracket2, btn_ac, btn_equals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvResult = findViewById(R.id.tvResult);
        tvExpression = findViewById(R.id.tvExpression);

        initButton(btn_0, R.id.btn_0);
        initButton(btn_1, R.id.btn_1);
        initButton(btn_2, R.id.btn_2);
        initButton(btn_3, R.id.btn_3);
        initButton(btn_4, R.id.btn_4);
        initButton(btn_5, R.id.btn_5);
        initButton(btn_6, R.id.btn_6);
        initButton(btn_7, R.id.btn_7);
        initButton(btn_8, R.id.btn_8);
        initButton(btn_9, R.id.btn_9);
        initButton(btn_dot, R.id.btn_dot);
        initButton(btn_ac, R.id.btn_ac);
        initButton(btn_C, R.id.btn_C);
        initButton(btn_plus, R.id.btn_plus);
        initButton(btn_minus, R.id.btn_minus);
        initButton(btn_divide, R.id.btn_divide);
        initButton(btn_multiply, R.id.btn_multiply);
        initButton(btn_bracket1, R.id.btn_bracket1);
        initButton(btn_bracket2, R.id.btn_bracket2);
        initButton(btn_equals, R.id.btn_equals);
    }

    void initButton(MaterialButton button, int id) {
        button = findViewById(id);
        button.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();
        String data = tvExpression.getText().toString();


        if (buttonText.equals("AC")) {
            tvExpression.setText("0");
            tvResult.setText("0");
            data = "";
            return;
        }


        if (buttonText.equals("C")) {
            if (data.length() > 1) {
                data = data.substring(0, data.length() - 1);
            } else {
                data = "0";
            }
            tvExpression.setText(data);
            return;
        }

        if (buttonText.equals("=")) {
            String finalResult = evaluateExpression(data);
            tvResult.setText(finalResult);
            return;
        }

        if (data.equals("0")) {
            data = "";
        }
        data += buttonText;
        tvExpression.setText(data);
    }

    private String evaluateExpression(String expression) {
        Context rhino = Context.enter();
        rhino.setOptimizationLevel(-1);
        try {
            Scriptable scope = rhino.initStandardObjects();
            String result = rhino.evaluateString(scope, expression, "JavaScript", 1, null).toString();

            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            return decimalFormat.format(Double.parseDouble(result));
        } catch (Exception e) {
            return "Error";
        } finally {
            Context.exit();
        }
    }
}
