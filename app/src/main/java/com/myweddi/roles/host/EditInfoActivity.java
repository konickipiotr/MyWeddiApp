package com.myweddi.roles.host;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.myweddi.R;
import com.myweddi.module.weddinginfo.WeddingInfo;
import com.myweddi.roles.WeddingInfoActivity;
import com.myweddi.settings.Settings;
import com.myweddi.utils.DateUtils;
import com.myweddi.utils.RequestUtils;
import com.myweddi.utils.Utils;
import com.squareup.picasso.Picasso;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class EditInfoActivity extends AppCompatActivity {

    public WeddingInfo weddingInfo;

    private final int CAMERA_CHU_SRC = 100;
    private final int CAMERA_WEDD_SRC = 101;
    private final int STORAGE_CHU_SRC = 200;
    private final int STORAGE_WEDD_SRC = 201;

    final Calendar myCalendar = Calendar.getInstance();

    private Bitmap churchBitmap, weddingHBitmap;
    private ImageView churchImg, weddingHImg;
    private boolean churchImgChanged, weddingImgChanged;

    private ImageButton bInfoTakeChurchPhoto, bInfoTakeWeddinghPhoto;
    private ImageButton editinfo_church_from_storage, editinfo_weddingplace_from_storage;
    private ImageButton editinfo_church_clean, editinfo_weddingplace_clean;
    private Button bWeddingInfoSave, bWeddingInfoCancel;

    private EditText editinfo_weddingdate, editinfo_weddingtime, editinfo_church, editinfo_church_address,
            editinfo_wedding_address, editinfo_wedding_place, editinfo_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        init();
        initListeners();

        if(ContextCompat.checkSelfPermission(EditInfoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditInfoActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, CAMERA_CHU_SRC);
        }

        if(ContextCompat.checkSelfPermission(EditInfoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditInfoActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, CAMERA_WEDD_SRC);
        }

        new FetchInfoAsync(this).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null) return;

        if(requestCode == CAMERA_CHU_SRC || requestCode == CAMERA_WEDD_SRC){

            Bitmap newbitmap = (Bitmap) data.getExtras().get("data");
            if(requestCode == CAMERA_CHU_SRC){
                churchBitmap = newbitmap;
                churchImg.setImageBitmap(churchBitmap);
                churchImg.setVisibility(View.VISIBLE);
                churchImgChanged = true;
            }else if(requestCode == CAMERA_WEDD_SRC){
                weddingHBitmap = newbitmap;
                weddingHImg.setImageBitmap(weddingHBitmap);
                weddingHImg.setVisibility(View.VISIBLE);
                weddingImgChanged = true;
            }

        }else if(requestCode == STORAGE_CHU_SRC || requestCode == STORAGE_WEDD_SRC){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap newbitmap = BitmapFactory.decodeStream(imageStream);

                if(requestCode == STORAGE_CHU_SRC){
                    churchBitmap = newbitmap;
                    churchImg.setImageBitmap(churchBitmap);
                    churchImg.setVisibility(View.VISIBLE);
                    churchImgChanged = true;
                }else if(requestCode == STORAGE_WEDD_SRC){
                    weddingHBitmap = newbitmap;
                    weddingHImg.setImageBitmap(weddingHBitmap);
                    weddingHImg.setVisibility(View.VISIBLE);
                    weddingImgChanged = true;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void init(){
        churchImg = findViewById(R.id.editinfo_church_img);
        weddingHImg = findViewById(R.id.editinfo_weddingplace_img);
        bInfoTakeChurchPhoto = findViewById(R.id.editinfo_church_takephoto);
        bInfoTakeWeddinghPhoto = findViewById(R.id.editinfo_weddingplace_takephoto);
        editinfo_church_from_storage = findViewById(R.id.editinfo_church_from_storage);
        editinfo_weddingplace_from_storage = findViewById(R.id.editinfo_weddingplace_from_storage);
        editinfo_church_clean = findViewById(R.id.editinfo_church_clean);
        editinfo_weddingplace_clean = findViewById(R.id.editinfo_weddingplace_clean);

        editinfo_weddingdate = findViewById(R.id.editinfo_weddingdate);
        editinfo_weddingtime = findViewById(R.id.editinfo_weddingtime);
        editinfo_church = findViewById(R.id.editinfo_church);
        editinfo_church_address = findViewById(R.id.editinfo_church_address);

        editinfo_wedding_address = findViewById(R.id.editinfo_wedding_place);
        editinfo_wedding_place = findViewById(R.id.editinfo_wedding_address);
        editinfo_info = findViewById(R.id.editinfo_info);

        bWeddingInfoSave = findViewById(R.id.bWeddingInfoSave);
        bWeddingInfoCancel = findViewById(R.id.bWeddingInfoCancel);

        churchImg.setVisibility(View.GONE);
        weddingHImg.setVisibility(View.GONE);
    }

    private void  initListeners(){
        bInfoTakeChurchPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_CHU_SRC);
        });

        bInfoTakeWeddinghPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_WEDD_SRC);
        });

        editinfo_church_from_storage.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, STORAGE_CHU_SRC);
        });

        editinfo_weddingplace_from_storage.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, STORAGE_WEDD_SRC);
        });

        editinfo_church_clean.setOnClickListener(v -> {
            churchImg.setImageBitmap(null);
            churchImg.setVisibility(View.GONE);
            churchBitmap = null;
        });

        editinfo_weddingplace_clean.setOnClickListener(v -> {
            weddingHImg.setImageBitmap(null);
            weddingHImg.setVisibility(View.GONE);
            weddingHBitmap = null;
        });

        bWeddingInfoSave.setOnClickListener(v -> {
            if(weddingInfo == null)
                weddingInfo = new WeddingInfo(Settings.weddingid);
            weddingInfo.setCeremenytime(DateUtils.getWeddingLocalDateTime(editinfo_weddingdate, editinfo_weddingtime));
            weddingInfo.setChurchname(editinfo_church.getText().toString());
            weddingInfo.setChurchaddress(editinfo_church_address.getText().toString());
            weddingInfo.setWeddinghousename(editinfo_wedding_place.getText().toString());
            weddingInfo.setwAddress(editinfo_wedding_address.getText().toString());
            weddingInfo.setInfo(editinfo_info.getText().toString());

            new SaveWeddingInfoAsync(EditInfoActivity.this).execute(weddingInfo);
            finish();
            startActivity(new Intent(EditInfoActivity.this, WeddingInfoActivity.class));
        });

        bWeddingInfoCancel.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(EditInfoActivity.this, WeddingInfoActivity.class));
        });

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        editinfo_weddingdate.setOnClickListener(v -> new DatePickerDialog(EditInfoActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        editinfo_weddingdate.setText(sdf.format(myCalendar.getTime()));
    }

    private static class FetchInfoAsync extends AsyncTask<Void, Void, WeddingInfo> {

        private final WeakReference<EditInfoActivity> activityReference;

        public FetchInfoAsync(EditInfoActivity context) {
            this.activityReference = new WeakReference<>(context);
        }

        @Override
        protected WeddingInfo doInBackground(Void... params) {

            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();

            String path = Settings.server_url + "/api/weddinginfo/" + Settings.weddingid;
            ResponseEntity<WeddingInfo> response = restTemplate.exchange(path,
                    HttpMethod.GET,
                    new HttpEntity<Object>(requestHeaders),
                    WeddingInfo.class);

            WeddingInfo weddingInfo = response.getBody();
            return weddingInfo;
        }

        @Override
        protected void onPostExecute(WeddingInfo weddingInfo) {
            super.onPostExecute(weddingInfo);
            EditInfoActivity context = activityReference.get();

            context.weddingInfo = weddingInfo;
            context.editinfo_weddingdate.setText(DateUtils.getWeddingDate(weddingInfo));
            context.editinfo_weddingtime.setText(DateUtils.getWeddingTime(weddingInfo));
            context.editinfo_church.setText(weddingInfo.getChurchname());
            context.editinfo_church_address.setText(weddingInfo.getChurchaddress());

            if(weddingInfo.getwWebAppPath() != null) {
                String path = Settings.server_url + weddingInfo.getwWebAppPath();
                Picasso.get().load(path).into(context.weddingHImg);
                context.weddingHImg.setVisibility(View.VISIBLE);
            }

            if(weddingInfo.getChWebAppPath() != null) {
                String path = Settings.server_url + weddingInfo.getChWebAppPath();
                Picasso.get().load(path).into(context.churchImg);
                context.churchImg.setVisibility(View.VISIBLE);
            }

            context.editinfo_wedding_place.setText(weddingInfo.getWeddinghousename());
            context.editinfo_wedding_address.setText(weddingInfo.getwAddress());
            context.editinfo_info.setText(weddingInfo.getInfo());
        }
    }


    private static class SaveWeddingInfoAsync extends AsyncTask<WeddingInfo, Void, Void> {

        private final WeakReference<EditInfoActivity> activityReference;

        public SaveWeddingInfoAsync(EditInfoActivity context) {
            this.activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(WeddingInfo... params) {
            EditInfoActivity context = activityReference.get();
            RequestUtils requestUtils = new RequestUtils();
            RestTemplate restTemplate = requestUtils.getRestTemplate();
            HttpHeaders requestHeaders = requestUtils.getRequestHeaders();
            String ROOT_PATH = Settings.server_url + "/api/weddinginfo/";

            String path = ROOT_PATH;
            ResponseEntity<Long> response = restTemplate.exchange(path,
                    HttpMethod.POST,
                    new HttpEntity<>(params[0], requestHeaders),
                    Long.class);

            Long weddingId = response.getBody();

            String photo;
            HttpEntity<Object> request;
            if(context.churchImgChanged) {
                photo = Utils.imgToString(context.churchBitmap);
                request = new HttpEntity<>(photo, requestHeaders);
                path = ROOT_PATH + weddingId + "/churchphoto";
                ResponseEntity<Long> response1 = restTemplate.exchange(path,
                        HttpMethod.POST,
                        request,
                        Long.class);
            }

            if(context.weddingImgChanged) {
                photo = Utils.imgToString(context.weddingHBitmap);
                request = new HttpEntity<>(photo, requestHeaders);
                path = ROOT_PATH + weddingId + "/weddinghousephoto";
                ResponseEntity<Long> response2 = restTemplate.exchange(path,
                        HttpMethod.POST,
                        request,
                        Long.class);
            }

            return null;
        }
    }
}
