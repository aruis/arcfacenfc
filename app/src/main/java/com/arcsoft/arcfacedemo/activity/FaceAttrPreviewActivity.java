package com.arcsoft.arcfacedemo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.model.DrawInfo;
import com.arcsoft.arcfacedemo.util.ConfigUtil;
import com.arcsoft.arcfacedemo.util.DrawHelper;
import com.arcsoft.arcfacedemo.util.camera.CameraHelper;
import com.arcsoft.arcfacedemo.util.camera.CameraListener;
import com.arcsoft.arcfacedemo.util.face.RecognizeColor;
import com.arcsoft.arcfacedemo.widget.FaceRectView;
import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.enums.DetectMode;

import java.util.ArrayList;
import java.util.List;

public class FaceAttrPreviewActivity extends BaseActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = "FaceAttrPreviewActivity";
    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;
    private Camera.Size previewSize;
    private Integer rgbCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private FaceEngine faceEngine;

    private byte[] myface = new byte[1032];


    private int afCode = -1;
    private int processMask = FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS;
    /**
     * 相机预览显示的控件，可为SurfaceView或TextureView
     */
    private View previewView;
    private FaceRectView faceRectView;

    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    /**
     * 所需的所有权限信息
     */
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };

    private void initMyFace() {
        myface[0] = 0;
        myface[1] = -128;
        myface[2] = -6;
        myface[3] = 68;
        myface[4] = 0;
        myface[5] = 0;
        myface[6] = -96;
        myface[7] = 65;
        myface[8] = 111;
        myface[9] = 110;
        myface[10] = 22;
        myface[11] = 60;
        myface[12] = -115;
        myface[13] = 4;
        myface[14] = 26;
        myface[15] = -66;
        myface[16] = -67;
        myface[17] = -69;
        myface[18] = -22;
        myface[19] = -67;
        myface[20] = 67;
        myface[21] = -45;
        myface[22] = -32;
        myface[23] = 61;
        myface[24] = 111;
        myface[25] = -103;
        myface[26] = 46;
        myface[27] = -68;
        myface[28] = -107;
        myface[29] = -43;
        myface[30] = -91;
        myface[31] = -67;
        myface[32] = 121;
        myface[33] = 64;
        myface[34] = -127;
        myface[35] = -69;
        myface[36] = 25;
        myface[37] = -100;
        myface[38] = 6;
        myface[39] = 61;
        myface[40] = -17;
        myface[41] = 40;
        myface[42] = 46;
        myface[43] = -67;
        myface[44] = -60;
        myface[45] = -113;
        myface[46] = -85;
        myface[47] = 61;
        myface[48] = -11;
        myface[49] = -58;
        myface[50] = -72;
        myface[51] = -67;
        myface[52] = 72;
        myface[53] = 100;
        myface[54] = -25;
        myface[55] = 61;
        myface[56] = 114;
        myface[57] = 123;
        myface[58] = 84;
        myface[59] = 61;
        myface[60] = 89;
        myface[61] = 12;
        myface[62] = 107;
        myface[63] = 61;
        myface[64] = -18;
        myface[65] = 54;
        myface[66] = 121;
        myface[67] = -68;
        myface[68] = 13;
        myface[69] = -69;
        myface[70] = 89;
        myface[71] = -67;
        myface[72] = 63;
        myface[73] = -111;
        myface[74] = 56;
        myface[75] = -67;
        myface[76] = 109;
        myface[77] = 50;
        myface[78] = 91;
        myface[79] = -67;
        myface[80] = -42;
        myface[81] = -57;
        myface[82] = 59;
        myface[83] = -67;
        myface[84] = 58;
        myface[85] = -72;
        myface[86] = -121;
        myface[87] = 60;
        myface[88] = 108;
        myface[89] = -63;
        myface[90] = 58;
        myface[91] = 61;
        myface[92] = 106;
        myface[93] = 106;
        myface[94] = 54;
        myface[95] = 60;
        myface[96] = -44;
        myface[97] = -110;
        myface[98] = 22;
        myface[99] = 62;
        myface[100] = 101;
        myface[101] = 122;
        myface[102] = 6;
        myface[103] = -68;
        myface[104] = -42;
        myface[105] = -3;
        myface[106] = -38;
        myface[107] = -67;
        myface[108] = -51;
        myface[109] = 125;
        myface[110] = -96;
        myface[111] = 60;
        myface[112] = 60;
        myface[113] = -55;
        myface[114] = -86;
        myface[115] = -67;
        myface[116] = 89;
        myface[117] = -81;
        myface[118] = -80;
        myface[119] = 61;
        myface[120] = 64;
        myface[121] = 31;
        myface[122] = -65;
        myface[123] = 59;
        myface[124] = -23;
        myface[125] = -40;
        myface[126] = 52;
        myface[127] = -68;
        myface[128] = -3;
        myface[129] = -12;
        myface[130] = -116;
        myface[131] = -68;
        myface[132] = -119;
        myface[133] = -117;
        myface[134] = -82;
        myface[135] = 61;
        myface[136] = -8;
        myface[137] = 1;
        myface[138] = -34;
        myface[139] = -67;
        myface[140] = -123;
        myface[141] = 59;
        myface[142] = -26;
        myface[143] = -67;
        myface[144] = 32;
        myface[145] = -10;
        myface[146] = -56;
        myface[147] = -67;
        myface[148] = -123;
        myface[149] = -12;
        myface[150] = -113;
        myface[151] = 61;
        myface[152] = 60;
        myface[153] = -42;
        myface[154] = -101;
        myface[155] = 60;
        myface[156] = 119;
        myface[157] = -8;
        myface[158] = -110;
        myface[159] = 60;
        myface[160] = -72;
        myface[161] = 44;
        myface[162] = 57;
        myface[163] = 62;
        myface[164] = -96;
        myface[165] = 73;
        myface[166] = 85;
        myface[167] = -67;
        myface[168] = 50;
        myface[169] = 69;
        myface[170] = 82;
        myface[171] = 60;
        myface[172] = -79;
        myface[173] = -2;
        myface[174] = -12;
        myface[175] = -67;
        myface[176] = 51;
        myface[177] = 78;
        myface[178] = -43;
        myface[179] = -67;
        myface[180] = -11;
        myface[181] = 94;
        myface[182] = 3;
        myface[183] = 61;
        myface[184] = 33;
        myface[185] = 64;
        myface[186] = -73;
        myface[187] = -68;
        myface[188] = -38;
        myface[189] = -10;
        myface[190] = -122;
        myface[191] = 59;
        myface[192] = -56;
        myface[193] = -6;
        myface[194] = 71;
        myface[195] = -67;
        myface[196] = -86;
        myface[197] = -5;
        myface[198] = 32;
        myface[199] = -67;
        myface[200] = 114;
        myface[201] = 14;
        myface[202] = -113;
        myface[203] = 61;
        myface[204] = -7;
        myface[205] = -75;
        myface[206] = -55;
        myface[207] = -67;
        myface[208] = 117;
        myface[209] = -17;
        myface[210] = -112;
        myface[211] = -67;
        myface[212] = 109;
        myface[213] = -77;
        myface[214] = -125;
        myface[215] = -67;
        myface[216] = -7;
        myface[217] = 46;
        myface[218] = -115;
        myface[219] = 61;
        myface[220] = 17;
        myface[221] = -31;
        myface[222] = -85;
        myface[223] = -68;
        myface[224] = -62;
        myface[225] = 102;
        myface[226] = 25;
        myface[227] = 61;
        myface[228] = -24;
        myface[229] = 7;
        myface[230] = -114;
        myface[231] = -67;
        myface[232] = 16;
        myface[233] = 38;
        myface[234] = 75;
        myface[235] = 60;
        myface[236] = -99;
        myface[237] = -124;
        myface[238] = 62;
        myface[239] = 61;
        myface[240] = -9;
        myface[241] = 116;
        myface[242] = 31;
        myface[243] = 61;
        myface[244] = -45;
        myface[245] = -13;
        myface[246] = 63;
        myface[247] = -67;
        myface[248] = -7;
        myface[249] = -80;
        myface[250] = -128;
        myface[251] = 61;
        myface[252] = -25;
        myface[253] = -110;
        myface[254] = -68;
        myface[255] = 61;
        myface[256] = -112;
        myface[257] = -97;
        myface[258] = -106;
        myface[259] = 61;
        myface[260] = 6;
        myface[261] = 24;
        myface[262] = -51;
        myface[263] = -68;
        myface[264] = -39;
        myface[265] = -5;
        myface[266] = -115;
        myface[267] = 61;
        myface[268] = 122;
        myface[269] = -3;
        myface[270] = 62;
        myface[271] = 62;
        myface[272] = -70;
        myface[273] = -43;
        myface[274] = -64;
        myface[275] = 60;
        myface[276] = 100;
        myface[277] = -86;
        myface[278] = 82;
        myface[279] = -68;
        myface[280] = 18;
        myface[281] = 80;
        myface[282] = 97;
        myface[283] = -67;
        myface[284] = 32;
        myface[285] = -123;
        myface[286] = 15;
        myface[287] = 61;
        myface[288] = -45;
        myface[289] = -93;
        myface[290] = -14;
        myface[291] = -68;
        myface[292] = 87;
        myface[293] = 22;
        myface[294] = -118;
        myface[295] = -67;
        myface[296] = -6;
        myface[297] = 68;
        myface[298] = -19;
        myface[299] = 60;
        myface[300] = -80;
        myface[301] = 13;
        myface[302] = 116;
        myface[303] = 59;
        myface[304] = -14;
        myface[305] = 99;
        myface[306] = -114;
        myface[307] = -67;
        myface[308] = -33;
        myface[309] = -39;
        myface[310] = 87;
        myface[311] = 61;
        myface[312] = 66;
        myface[313] = 19;
        myface[314] = -128;
        myface[315] = -68;
        myface[316] = 53;
        myface[317] = 27;
        myface[318] = 46;
        myface[319] = 60;
        myface[320] = 78;
        myface[321] = 108;
        myface[322] = 82;
        myface[323] = -68;
        myface[324] = 52;
        myface[325] = -117;
        myface[326] = 104;
        myface[327] = 60;
        myface[328] = -66;
        myface[329] = 12;
        myface[330] = -98;
        myface[331] = -67;
        myface[332] = 36;
        myface[333] = 54;
        myface[334] = -85;
        myface[335] = -67;
        myface[336] = 35;
        myface[337] = -76;
        myface[338] = -24;
        myface[339] = -68;
        myface[340] = 47;
        myface[341] = 60;
        myface[342] = -50;
        myface[343] = 59;
        myface[344] = 93;
        myface[345] = -24;
        myface[346] = -93;
        myface[347] = 60;
        myface[348] = -82;
        myface[349] = -15;
        myface[350] = 56;
        myface[351] = -67;
        myface[352] = 1;
        myface[353] = 65;
        myface[354] = -108;
        myface[355] = -68;
        myface[356] = -126;
        myface[357] = -29;
        myface[358] = 57;
        myface[359] = -67;
        myface[360] = 94;
        myface[361] = -118;
        myface[362] = 61;
        myface[363] = -68;
        myface[364] = -103;
        myface[365] = 24;
        myface[366] = 3;
        myface[367] = -67;
        myface[368] = -75;
        myface[369] = -10;
        myface[370] = 28;
        myface[371] = -67;
        myface[372] = 63;
        myface[373] = 24;
        myface[374] = 15;
        myface[375] = -67;
        myface[376] = 33;
        myface[377] = 3;
        myface[378] = 59;
        myface[379] = 61;
        myface[380] = 109;
        myface[381] = 65;
        myface[382] = -54;
        myface[383] = 60;
        myface[384] = -6;
        myface[385] = 58;
        myface[386] = 111;
        myface[387] = -69;
        myface[388] = -120;
        myface[389] = 0;
        myface[390] = -107;
        myface[391] = 61;
        myface[392] = -85;
        myface[393] = 35;
        myface[394] = -23;
        myface[395] = 59;
        myface[396] = -121;
        myface[397] = -63;
        myface[398] = -112;
        myface[399] = -68;
        myface[400] = -76;
        myface[401] = -104;
        myface[402] = 45;
        myface[403] = -67;
        myface[404] = -67;
        myface[405] = -111;
        myface[406] = -58;
        myface[407] = 60;
        myface[408] = -126;
        myface[409] = 24;
        myface[410] = -96;
        myface[411] = -67;
        myface[412] = 5;
        myface[413] = -108;
        myface[414] = 76;
        myface[415] = 61;
        myface[416] = -61;
        myface[417] = 87;
        myface[418] = 35;
        myface[419] = -67;
        myface[420] = -87;
        myface[421] = 97;
        myface[422] = -47;
        myface[423] = -67;
        myface[424] = -71;
        myface[425] = 84;
        myface[426] = -124;
        myface[427] = -68;
        myface[428] = -76;
        myface[429] = -115;
        myface[430] = -85;
        myface[431] = -68;
        myface[432] = -113;
        myface[433] = 19;
        myface[434] = -99;
        myface[435] = -67;
        myface[436] = -33;
        myface[437] = -20;
        myface[438] = 66;
        myface[439] = -67;
        myface[440] = 87;
        myface[441] = 18;
        myface[442] = 93;
        myface[443] = 59;
        myface[444] = 83;
        myface[445] = -113;
        myface[446] = 15;
        myface[447] = -67;
        myface[448] = 106;
        myface[449] = 11;
        myface[450] = -118;
        myface[451] = -67;
        myface[452] = 9;
        myface[453] = 11;
        myface[454] = 12;
        myface[455] = -68;
        myface[456] = 114;
        myface[457] = 57;
        myface[458] = -63;
        myface[459] = -67;
        myface[460] = 53;
        myface[461] = -109;
        myface[462] = -111;
        myface[463] = -67;
        myface[464] = -117;
        myface[465] = 11;
        myface[466] = 54;
        myface[467] = 61;
        myface[468] = 96;
        myface[469] = 106;
        myface[470] = -18;
        myface[471] = 58;
        myface[472] = 50;
        myface[473] = 90;
        myface[474] = -95;
        myface[475] = 61;
        myface[476] = -11;
        myface[477] = -117;
        myface[478] = -89;
        myface[479] = -67;
        myface[480] = 1;
        myface[481] = -99;
        myface[482] = 28;
        myface[483] = -68;
        myface[484] = -109;
        myface[485] = -39;
        myface[486] = 2;
        myface[487] = -66;
        myface[488] = -84;
        myface[489] = 48;
        myface[490] = 121;
        myface[491] = -67;
        myface[492] = -51;
        myface[493] = 108;
        myface[494] = 56;
        myface[495] = -67;
        myface[496] = 80;
        myface[497] = 127;
        myface[498] = 121;
        myface[499] = -67;
        myface[500] = 41;
        myface[501] = 28;
        myface[502] = -119;
        myface[503] = -67;
        myface[504] = -39;
        myface[505] = -12;
        myface[506] = 105;
        myface[507] = 61;
        myface[508] = -49;
        myface[509] = 23;
        myface[510] = 83;
        myface[511] = 61;
        myface[512] = -111;
        myface[513] = 51;
        myface[514] = -55;
        myface[515] = 61;
        myface[516] = 38;
        myface[517] = -41;
        myface[518] = 83;
        myface[519] = -68;
        myface[520] = 104;
        myface[521] = 37;
        myface[522] = 66;
        myface[523] = -67;
        myface[524] = -117;
        myface[525] = -92;
        myface[526] = 43;
        myface[527] = 61;
        myface[528] = -103;
        myface[529] = 81;
        myface[530] = -109;
        myface[531] = 61;
        myface[532] = 126;
        myface[533] = 52;
        myface[534] = 114;
        myface[535] = -67;
        myface[536] = -31;
        myface[537] = -123;
        myface[538] = 20;
        myface[539] = -68;
        myface[540] = -21;
        myface[541] = -28;
        myface[542] = -49;
        myface[543] = -68;
        myface[544] = -60;
        myface[545] = -79;
        myface[546] = -118;
        myface[547] = -67;
        myface[548] = 7;
        myface[549] = -27;
        myface[550] = -7;
        myface[551] = 61;
        myface[552] = 42;
        myface[553] = 43;
        myface[554] = -46;
        myface[555] = -68;
        myface[556] = 93;
        myface[557] = -6;
        myface[558] = -75;
        myface[559] = 60;
        myface[560] = 85;
        myface[561] = 72;
        myface[562] = 81;
        myface[563] = -66;
        myface[564] = 94;
        myface[565] = -121;
        myface[566] = -1;
        myface[567] = 60;
        myface[568] = 117;
        myface[569] = -1;
        myface[570] = 113;
        myface[571] = -67;
        myface[572] = 70;
        myface[573] = -9;
        myface[574] = -87;
        myface[575] = 60;
        myface[576] = 51;
        myface[577] = 104;
        myface[578] = 103;
        myface[579] = -67;
        myface[580] = -69;
        myface[581] = -47;
        myface[582] = 5;
        myface[583] = 62;
        myface[584] = -45;
        myface[585] = 56;
        myface[586] = 29;
        myface[587] = 61;
        myface[588] = 7;
        myface[589] = 31;
        myface[590] = 58;
        myface[591] = 61;
        myface[592] = -45;
        myface[593] = 90;
        myface[594] = -66;
        myface[595] = -68;
        myface[596] = 114;
        myface[597] = 64;
        myface[598] = -69;
        myface[599] = -67;
        myface[600] = 87;
        myface[601] = 54;
        myface[602] = 47;
        myface[603] = -67;
        myface[604] = 76;
        myface[605] = 82;
        myface[606] = -105;
        myface[607] = -67;
        myface[608] = -25;
        myface[609] = -29;
        myface[610] = 118;
        myface[611] = -68;
        myface[612] = -107;
        myface[613] = 28;
        myface[614] = -109;
        myface[615] = 60;
        myface[616] = -64;
        myface[617] = 76;
        myface[618] = 5;
        myface[619] = -66;
        myface[620] = 87;
        myface[621] = -74;
        myface[622] = 109;
        myface[623] = 61;
        myface[624] = -5;
        myface[625] = -54;
        myface[626] = 118;
        myface[627] = 61;
        myface[628] = 117;
        myface[629] = -68;
        myface[630] = 111;
        myface[631] = -68;
        myface[632] = -12;
        myface[633] = 88;
        myface[634] = 28;
        myface[635] = -67;
        myface[636] = -75;
        myface[637] = 47;
        myface[638] = -80;
        myface[639] = 59;
        myface[640] = 68;
        myface[641] = -89;
        myface[642] = -82;
        myface[643] = 60;
        myface[644] = -5;
        myface[645] = -54;
        myface[646] = 33;
        myface[647] = -67;
        myface[648] = 62;
        myface[649] = 42;
        myface[650] = 96;
        myface[651] = 61;
        myface[652] = 122;
        myface[653] = 44;
        myface[654] = -128;
        myface[655] = -67;
        myface[656] = -95;
        myface[657] = -37;
        myface[658] = -57;
        myface[659] = 60;
        myface[660] = -91;
        myface[661] = -36;
        myface[662] = -121;
        myface[663] = 60;
        myface[664] = -52;
        myface[665] = -3;
        myface[666] = 12;
        myface[667] = -66;
        myface[668] = 110;
        myface[669] = 117;
        myface[670] = -108;
        myface[671] = -67;
        myface[672] = 9;
        myface[673] = 100;
        myface[674] = 43;
        myface[675] = 60;
        myface[676] = 36;
        myface[677] = -101;
        myface[678] = -98;
        myface[679] = -67;
        myface[680] = -65;
        myface[681] = -102;
        myface[682] = -126;
        myface[683] = 60;
        myface[684] = 5;
        myface[685] = -36;
        myface[686] = 98;
        myface[687] = -69;
        myface[688] = -93;
        myface[689] = 124;
        myface[690] = -82;
        myface[691] = -67;
        myface[692] = -61;
        myface[693] = 62;
        myface[694] = 95;
        myface[695] = 61;
        myface[696] = 92;
        myface[697] = -73;
        myface[698] = -76;
        myface[699] = -67;
        myface[700] = 1;
        myface[701] = 126;
        myface[702] = 34;
        myface[703] = -66;
        myface[704] = -83;
        myface[705] = -126;
        myface[706] = 76;
        myface[707] = 60;
        myface[708] = -101;
        myface[709] = 36;
        myface[710] = -95;
        myface[711] = -67;
        myface[712] = 33;
        myface[713] = -128;
        myface[714] = -111;
        myface[715] = -67;
        myface[716] = -14;
        myface[717] = 122;
        myface[718] = 62;
        myface[719] = -68;
        myface[720] = -124;
        myface[721] = -43;
        myface[722] = -108;
        myface[723] = 60;
        myface[724] = -72;
        myface[725] = 34;
        myface[726] = -70;
        myface[727] = 61;
        myface[728] = 90;
        myface[729] = -71;
        myface[730] = -70;
        myface[731] = 60;
        myface[732] = -56;
        myface[733] = -86;
        myface[734] = 68;
        myface[735] = -69;
        myface[736] = -42;
        myface[737] = 40;
        myface[738] = 25;
        myface[739] = 61;
        myface[740] = -52;
        myface[741] = 43;
        myface[742] = 25;
        myface[743] = 61;
        myface[744] = 57;
        myface[745] = 125;
        myface[746] = -125;
        myface[747] = -69;
        myface[748] = 22;
        myface[749] = 51;
        myface[750] = -80;
        myface[751] = 61;
        myface[752] = -17;
        myface[753] = -88;
        myface[754] = 68;
        myface[755] = -67;
        myface[756] = 18;
        myface[757] = -62;
        myface[758] = 12;
        myface[759] = 62;
        myface[760] = 106;
        myface[761] = 66;
        myface[762] = -53;
        myface[763] = -68;
        myface[764] = 77;
        myface[765] = 83;
        myface[766] = 119;
        myface[767] = -67;
        myface[768] = -93;
        myface[769] = 114;
        myface[770] = -73;
        myface[771] = 61;
        myface[772] = -105;
        myface[773] = 101;
        myface[774] = -108;
        myface[775] = 60;
        myface[776] = 97;
        myface[777] = -102;
        myface[778] = -76;
        myface[779] = -67;
        myface[780] = 112;
        myface[781] = 115;
        myface[782] = 11;
        myface[783] = 61;
        myface[784] = 126;
        myface[785] = -67;
        myface[786] = -122;
        myface[787] = -68;
        myface[788] = -113;
        myface[789] = -41;
        myface[790] = -20;
        myface[791] = 61;
        myface[792] = 62;
        myface[793] = 52;
        myface[794] = 4;
        myface[795] = -67;
        myface[796] = 16;
        myface[797] = 19;
        myface[798] = -24;
        myface[799] = -68;
        myface[800] = 7;
        myface[801] = -117;
        myface[802] = 55;
        myface[803] = 61;
        myface[804] = 88;
        myface[805] = 3;
        myface[806] = 71;
        myface[807] = 61;
        myface[808] = -57;
        myface[809] = -87;
        myface[810] = -112;
        myface[811] = -69;
        myface[812] = 121;
        myface[813] = -79;
        myface[814] = -19;
        myface[815] = 60;
        myface[816] = 84;
        myface[817] = 91;
        myface[818] = -71;
        myface[819] = 59;
        myface[820] = 24;
        myface[821] = -73;
        myface[822] = 39;
        myface[823] = -68;
        myface[824] = -95;
        myface[825] = -70;
        myface[826] = -102;
        myface[827] = -67;
        myface[828] = 109;
        myface[829] = 87;
        myface[830] = -122;
        myface[831] = 61;
        myface[832] = 111;
        myface[833] = 18;
        myface[834] = 38;
        myface[835] = -67;
        myface[836] = -55;
        myface[837] = 19;
        myface[838] = -29;
        myface[839] = 61;
        myface[840] = 25;
        myface[841] = 19;
        myface[842] = 101;
        myface[843] = 61;
        myface[844] = -53;
        myface[845] = -31;
        myface[846] = -60;
        myface[847] = -67;
        myface[848] = -35;
        myface[849] = -89;
        myface[850] = -93;
        myface[851] = 60;
        myface[852] = -33;
        myface[853] = 96;
        myface[854] = 36;
        myface[855] = -67;
        myface[856] = 54;
        myface[857] = -100;
        myface[858] = 31;
        myface[859] = -67;
        myface[860] = 54;
        myface[861] = 25;
        myface[862] = -115;
        myface[863] = 60;
        myface[864] = -43;
        myface[865] = 75;
        myface[866] = -102;
        myface[867] = 59;
        myface[868] = 2;
        myface[869] = 33;
        myface[870] = -23;
        myface[871] = 61;
        myface[872] = -95;
        myface[873] = -10;
        myface[874] = 12;
        myface[875] = 61;
        myface[876] = 104;
        myface[877] = 68;
        myface[878] = 104;
        myface[879] = 61;
        myface[880] = -103;
        myface[881] = -104;
        myface[882] = -98;
        myface[883] = 61;
        myface[884] = 14;
        myface[885] = 44;
        myface[886] = 12;
        myface[887] = -67;
        myface[888] = 14;
        myface[889] = 62;
        myface[890] = -124;
        myface[891] = -67;
        myface[892] = -48;
        myface[893] = 91;
        myface[894] = -68;
        myface[895] = -68;
        myface[896] = 26;
        myface[897] = 64;
        myface[898] = -39;
        myface[899] = 61;
        myface[900] = -105;
        myface[901] = 6;
        myface[902] = 88;
        myface[903] = -67;
        myface[904] = 38;
        myface[905] = -78;
        myface[906] = 81;
        myface[907] = -67;
        myface[908] = 37;
        myface[909] = -51;
        myface[910] = 23;
        myface[911] = 61;
        myface[912] = 107;
        myface[913] = -110;
        myface[914] = -47;
        myface[915] = -68;
        myface[916] = -19;
        myface[917] = -108;
        myface[918] = 86;
        myface[919] = -67;
        myface[920] = 68;
        myface[921] = -114;
        myface[922] = 43;
        myface[923] = 61;
        myface[924] = -126;
        myface[925] = 124;
        myface[926] = -112;
        myface[927] = 61;
        myface[928] = 54;
        myface[929] = -73;
        myface[930] = 42;
        myface[931] = -69;
        myface[932] = -46;
        myface[933] = -117;
        myface[934] = 61;
        myface[935] = -66;
        myface[936] = -58;
        myface[937] = 107;
        myface[938] = 25;
        myface[939] = 62;
        myface[940] = 102;
        myface[941] = -66;
        myface[942] = -72;
        myface[943] = -68;
        myface[944] = -42;
        myface[945] = 80;
        myface[946] = 0;
        myface[947] = -67;
        myface[948] = 89;
        myface[949] = 123;
        myface[950] = 66;
        myface[951] = 60;
        myface[952] = 24;
        myface[953] = -2;
        myface[954] = 60;
        myface[955] = -67;
        myface[956] = 59;
        myface[957] = 38;
        myface[958] = 56;
        myface[959] = -67;
        myface[960] = -68;
        myface[961] = -125;
        myface[962] = 97;
        myface[963] = 59;
        myface[964] = -63;
        myface[965] = -31;
        myface[966] = -15;
        myface[967] = 57;
        myface[968] = 76;
        myface[969] = -45;
        myface[970] = -50;
        myface[971] = 61;
        myface[972] = 92;
        myface[973] = -51;
        myface[974] = -114;
        myface[975] = 60;
        myface[976] = 73;
        myface[977] = 33;
        myface[978] = 50;
        myface[979] = 60;
        myface[980] = 74;
        myface[981] = 76;
        myface[982] = -100;
        myface[983] = -69;
        myface[984] = -75;
        myface[985] = -16;
        myface[986] = 96;
        myface[987] = -67;
        myface[988] = -1;
        myface[989] = -5;
        myface[990] = 44;
        myface[991] = 61;
        myface[992] = 17;
        myface[993] = 75;
        myface[994] = -103;
        myface[995] = 61;
        myface[996] = 5;
        myface[997] = -41;
        myface[998] = -46;
        myface[999] = 58;
        myface[1000] = 12;
        myface[1001] = -26;
        myface[1002] = 3;
        myface[1003] = 61;
        myface[1004] = 11;
        myface[1005] = 86;
        myface[1006] = 80;
        myface[1007] = -67;
        myface[1008] = -25;
        myface[1009] = 114;
        myface[1010] = 12;
        myface[1011] = 61;
        myface[1012] = -20;
        myface[1013] = -87;
        myface[1014] = 9;
        myface[1015] = 62;
        myface[1016] = -93;
        myface[1017] = 126;
        myface[1018] = -10;
        myface[1019] = 61;
        myface[1020] = 36;
        myface[1021] = -95;
        myface[1022] = -104;
        myface[1023] = 61;
        myface[1024] = 96;
        myface[1025] = 82;
        myface[1026] = -107;
        myface[1027] = -68;
        myface[1028] = -84;
        myface[1029] = 29;
        myface[1030] = 124;
        myface[1031] = -68;
    }

    Activity me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        me = this;
        initMyFace();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_attr_preview);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            getWindow().setAttributes(attributes);
        }

        // Activity启动后就锁定为启动时的方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        previewView = findViewById(R.id.texture_preview);
        faceRectView = findViewById(R.id.face_rect_view);
        //在布局结束后才做初始化操作
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    private void initEngine() {
        faceEngine = new FaceEngine();
        afCode = faceEngine.init(this, DetectMode.ASF_DETECT_MODE_VIDEO, ConfigUtil.getFtOrient(this),
                16, 20,
                FaceEngine.ASF_FACE_DETECT
                        | FaceEngine.ASF_AGE
                        | FaceEngine.ASF_FACE3DANGLE
                        | FaceEngine.ASF_GENDER
                        | FaceEngine.ASF_LIVENESS
                        | FaceEngine.ASF_FACE_RECOGNITION
        );
        Log.i(TAG, "initEngine:  init: " + afCode);
        if (afCode != ErrorInfo.MOK) {
            showToast(getString(R.string.init_failed, afCode));
        }
    }

    private void unInitEngine() {

        if (afCode == 0) {
            afCode = faceEngine.unInit();
            Log.i(TAG, "unInitEngine: " + afCode);
        }
    }


    @Override
    protected void onDestroy() {
        if (cameraHelper != null) {
            cameraHelper.release();
            cameraHelper = null;
        }
        unInitEngine();
        super.onDestroy();
    }

    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                Log.i(TAG, "onCameraOpened: " + cameraId + "  " + displayOrientation + " " + isMirror);
                previewSize = camera.getParameters().getPreviewSize();
                drawHelper = new DrawHelper(previewSize.width, previewSize.height, previewView.getWidth(), previewView.getHeight(), displayOrientation
                        , cameraId, isMirror, false, false);
            }


            @Override
            public void onPreview(byte[] nv21, Camera camera) {

                if (faceRectView != null) {
                    faceRectView.clearFaceInfo();
                }
                List<FaceInfo> faceInfoList = new ArrayList<>();
//                long start = System.currentTimeMillis();
                int code = faceEngine.detectFaces(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList);
                if (code == ErrorInfo.MOK && faceInfoList.size() > 0) {
                    code = faceEngine.process(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList, processMask);
                    if (code != ErrorInfo.MOK) {
                        return;
                    }
                } else {
                    return;
                }

                List<AgeInfo> ageInfoList = new ArrayList<>();
                List<GenderInfo> genderInfoList = new ArrayList<>();
                List<Face3DAngle> face3DAngleList = new ArrayList<>();
                List<LivenessInfo> faceLivenessInfoList = new ArrayList<>();
                int ageCode = faceEngine.getAge(ageInfoList);
                int genderCode = faceEngine.getGender(genderInfoList);
                int face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList);
                int livenessCode = faceEngine.getLiveness(faceLivenessInfoList);

                // 有其中一个的错误码不为ErrorInfo.MOK，return
                if ((ageCode | genderCode | face3DAngleCode | livenessCode) != ErrorInfo.MOK) {
                    return;
                }


                if (faceRectView != null && drawHelper != null) {
                    List<DrawInfo> drawInfoList = new ArrayList<>();
                    for (int i = 0; i < faceInfoList.size(); i++) {
                        drawInfoList.add(new DrawInfo(drawHelper.adjustRect(faceInfoList.get(i).getRect()), genderInfoList.get(i).getGender(), ageInfoList.get(i).getAge(), faceLivenessInfoList.get(i).getLiveness(), RecognizeColor.COLOR_UNKNOWN, null));
                    }
                    drawHelper.draw(faceRectView, drawInfoList);
                }

                FaceFeature faceFeature = new FaceFeature();
// 在FaceFeature的二进制数组中保存获取到的人脸特征数据
                int extractCode = faceEngine.extractFaceFeature(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList.get(0), faceFeature);
                if (extractCode == ErrorInfo.MOK) {
//                    showToast("extract face feature success");

                    this.onCameraClosed();
                    Intent intent = new Intent(me, MainActivity.class);

                    intent.putExtra("faceFeature", faceFeature.getFeatureData());

                    startActivity(intent);

                    Log.i(TAG, "extract face feature success");
                } else {
                    showToast("extract face feature failed, code is : " + extractCode);
                    Log.i(TAG, "extract face feature failed, code is : " + extractCode);
                }
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelper != null) {
                    drawHelper.setCameraDisplayOrientation(displayOrientation);
                }
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };
        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(previewView.getMeasuredWidth(), previewView.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(rgbCameraId != null ? rgbCameraId : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(previewView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
        cameraHelper.start();
    }

    @Override
    void afterRequestPermission(int requestCode, boolean isAllGranted) {
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            if (isAllGranted) {
                initEngine();
                initCamera();
            } else {
                showToast(getString(R.string.permission_denied));
            }
        }
    }

    /**
     * 在{@link #previewView}第一次布局完成后，去除该监听，并且进行引擎和相机的初始化
     */
    @Override
    public void onGlobalLayout() {
        previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        } else {
            initEngine();
            initCamera();
        }
    }


    /**
     * 切换相机。注意：若切换相机发现检测不到人脸，则极有可能是检测角度导致的，需要销毁引擎重新创建或者在设置界面修改配置的检测角度
     *
     * @param view
     */
    public void switchCamera(View view) {
        if (cameraHelper != null) {
            boolean success = cameraHelper.switchCamera();
            if (!success) {
                showToast(getString(R.string.switch_camera_failed));
            } else {
                showLongToast(getString(R.string.notice_change_detect_degree));
            }
        }
    }
}
