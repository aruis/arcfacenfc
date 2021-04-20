package com.arcsoft.arcfacedemo.activity;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.model.Ticket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static NfcAdapter mNfcAdapter;
    static MifareClassic currentMF1 = null;


    EditText sourceEdit;
    EditText targetEdit;
    DatePicker datePicker;

    byte[] face;

    List<Ticket> ticketList = new ArrayList<>();

    static String TAG = "bilibili";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sourceEdit = findViewById(R.id.source);
        targetEdit = findViewById(R.id.target);
        datePicker = findViewById(R.id.date);

        initNFC();

        Intent intent = getIntent();
        this.onNewIntent(intent);
    }

    protected void showLongToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    public void saolian(View view) {
        startActivityForResult(new Intent(this, FaceAttrPreviewActivity.class), 1);
    }

    boolean initNFC() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "对不起，您的设备不支持NFC，无法正常使用本软件",
                    Toast.LENGTH_LONG).show();
        } else if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "请到系统设置，开启NFC功能", Toast.LENGTH_LONG).show();
        } else {
            return true;
        }

        return false;
    }

    private boolean writeMF1(int sectorIndex, int blockIndex, byte[] key, byte[] data) {
        MifareClassic mfc = currentMF1;
        try {
            mfc.connect();
            boolean isOpen = mfc.authenticateSectorWithKeyA(sectorIndex, key);
            if (isOpen) {
                mfc.writeBlock(mfc.sectorToBlock(sectorIndex) + blockIndex, data);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private byte[] readMF1(int sectorIndex, int blockIndex, byte[] key) {
        MifareClassic mfc = currentMF1;
        try {
            mfc.connect();
            boolean isOpen = mfc.authenticateSectorWithKeyA(sectorIndex, key);
            Log.w(TAG, "readMF1: is OPen" + isOpen);
            if (isOpen) {
                return mfc.readBlock(mfc.sectorToBlock(sectorIndex) + blockIndex);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onNewIntent(Intent intent) {

        Log.d(TAG, "onNewIntent: M1");

        super.onNewIntent(intent);
        try {
            String action = intent.getAction();

            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                    || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                    || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

                Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


                byte[] MF1_UID = tagFromIntent.getId();

                MifareClassic mfc = MifareClassic.get(tagFromIntent);

                if (mfc == null) return;

                currentMF1 = mfc;

                mfc.connect();
                boolean isWhite = mfc.authenticateSectorWithKeyA(2, MifareClassic.KEY_DEFAULT);
                int blockCount = mfc.getBlockCount();
                int sectorCount = mfc.getSectorCount();

                HashMap map = new HashMap();
                map.put("uid", MF1_UID);
                map.put("isWhite", isWhite);
                map.put("sectorCount", sectorCount);
                map.put("unitBlockCount", blockCount / sectorCount);


                Log.d(TAG, "onNewIntent: uid" + MF1_UID);

                mfc.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (intent == null) return;

        this.face = intent.getByteArrayExtra("faceFeature");
        if (this.face != null) {
            Log.i(TAG, "get face");
        } else {
            Log.i(TAG, "no face");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {


        String source = sourceEdit.getText().toString();
        String target = targetEdit.getText().toString();

        Log.w(TAG, "onSaveInstanceState: " + source);

        outState.putString("source", source);
        outState.putString("target", target);


        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        outState.putInt("year", year);
        outState.putInt("month", month);
        outState.putInt("day", day);


        Log.w(TAG, "onSaveInstanceState: OK");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.w(TAG, "onRestoreInstanceState: " + savedInstanceState.getString("source"));

        super.onRestoreInstanceState(savedInstanceState);

        Log.w(TAG, "onRestoreInstanceState: " + savedInstanceState.getString("source"));

        sourceEdit.setText(savedInstanceState.getString("source"));


    }

    public void chupiao(View view) {

        String source = sourceEdit.getText().toString();
        String target = targetEdit.getText().toString();

        if (isBlank(source)) {
            showLongToast("请输入出发地。");
            return;
        }

        if (isBlank(target)) {
            showLongToast("请输入目的地。");
            return;
        }

        if (face == null) {
            showLongToast("请先扫描人脸信息。");
            return;
        }


        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        String id = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        Ticket ticket = new Ticket(id, source, target, year, month, day, this.face);

        ticketList.add(ticket);

        sourceEdit.getText().clear();
        targetEdit.getText().clear();
        this.face = null;


//        byte[] test = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        writeMF1(1, 0, MifareClassic.KEY_DEFAULT, id.getBytes());

        showLongToast("当前有" + ticketList.size() + "张票");
    }

    boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

}
