package com.curvello.fabricio.tomaagua;

import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvRelogio;
    private EditText etMensagem;
    private Button btCriarAlarme;

    private int hora;
    private int minuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComponentes();
    }

    private void iniciarComponentes() {
        tvRelogio = findViewById(R.id.tvRelogio);
        etMensagem = findViewById(R.id.etMensagem);
        btCriarAlarme = findViewById(R.id.btCriarAlarme);
    }

    //Aciona a View timePickerDialog para que o usuário defina a hora do alarme
    public void definirHoraAlarme(View view) {
        Calendar calendar = Calendar.getInstance();
        hora = calendar.get(Calendar.HOUR_OF_DAY);
        minuto = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                (timePicker, horaDoDia, minutos) -> {
                    String h = getString(R.string.numero_dois_digitos, horaDoDia);
                    String m = getString(R.string.numero_dois_digitos, minutos);
                    hora = Integer.parseInt(h);
                    minuto = Integer.parseInt(m);
                    tvRelogio.setText(getString(R.string.hora_doispontos_minutos,
                            h,
                            m));
                }, hora, minuto, true);
        timePickerDialog.show();
    }

    /*
      ATENÇÃO !!!
      NÃO ESQUECER DE INCLUIR A PERMISSÃO NO ANDROID MANIFEST:
      ANTES DE <application !!!

      <uses-permission android:name="com.android.alarm.permission.SET_ALARM" />

      VALIDAR SE A PERMMISÃO É EXATAMENTE ESSA. É COMUM AO DIGITAR O AS SUGERIR OUTRA PARECIDA.
    */


    public void criarAlarme(View view) {
        minimizaTeclado(btCriarAlarme);

        String mensagem;
        if (!etMensagem.getText().toString().isEmpty()) {
            mensagem = etMensagem.getText().toString();
        } else {
            mensagem = getString(R.string.toma_agua);
        }

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hora);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minuto);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, mensagem);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            Toast.makeText(MainActivity.this,
                    getString(R.string.app_nao_existe),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void minimizaTeclado(Button button) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(button.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}