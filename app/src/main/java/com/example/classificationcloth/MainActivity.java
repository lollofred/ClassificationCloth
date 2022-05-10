package com.example.classificationcloth;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




//import com.example.classificationcloth.ml.Mnist;
import com.example.classificationcloth.ml.Mnist;
import com.example.classificationcloth.ml.MobilenetV110224Quant;
import com.example.classificationcloth.ml.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
//import com.theartofdev.edmodo.cropper.CropImage;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView imgView;
    private Button btnSelect, btnPredict;
    private TextView tvResult;
    private TextView search;
    private Bitmap img;
    private FloatingActionButton fab;
    private String FinalString;
    private int counter =0;
    private boolean backPress = false;
    private String labelNotCloth = "Not a Cloth";
    private boolean imgChanged = true;

    Integer REQUEST_CAMERA=1, SELECT_FILE=0;


    /*
    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult ActivityResult) {
                            int resultCode = ActivityResult.getResultCode();
                            Intent data = ActivityResult.getData();
                            if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                                if (resultCode == RESULT_OK) {
                                    //imgView.setImageURI(data.getData());

                                    Uri uri = data.getData();
                                    try {
                                        img = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    imgView.setImageBitmap(img);
                                }
                            }

                            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                            {
                                Exception e = result.getError();
                                Toast.makeText(this,"AIUTOOOOOO!: " + e, Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
            );
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = (ImageView) findViewById(R.id.imageView);
        tvResult = (TextView) findViewById(R.id.tvResult);
        search = (TextView) findViewById(R.id.search);
        //btnSelect = (Button) findViewById(R.id.btnSelect);
        btnPredict = (Button) findViewById(R.id.btnPredict);
        fab = findViewById(R.id.fab);


/*

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //startActivityForResult(intent, 100);
                activityResultLauncher.launch(intent);

            }
        });


*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SelectImage();
            }
        });


        /*
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectImage();

                if(tvResult.getText().toString() != "")
                {
                    tvResult.setText("");
                    search.setVisibility(View.INVISIBLE);
                }


            }
        });
*/

        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                 //if (FieldNotBlank()) {
                //img = toGrayscale(img);

                Python py = Python.getInstance();

                //here we call our script with the name "myscirpt
                PyObject pyobj = py.getModule("grayscale");

                try {

                    String fileName = BitmapToFile(img);
                    PyObject obj = pyobj.callAttr("main", fileName);

                    byte data[] = obj.toJava(byte[].class);
                    img = BitmapFactory.decodeByteArray(data,0,data.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                                        if (false) {
                            img = Bitmap.createScaledBitmap(img, 224, 224, false);

                            MobilenetV110224Quant model = MobilenetV110224Quant.newInstance(getApplicationContext());

                            // Creates inputs for reference.
                            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);

                            TensorImage tensorImage = new TensorImage(DataType.UINT8);
                            tensorImage.load(img);
                            ByteBuffer byteBuffer = tensorImage.getBuffer();

                            inputFeature0.loadBuffer(byteBuffer);

                            // Runs model inference and gets result.
                            MobilenetV110224Quant.Outputs outputs = model.process(inputFeature0);
                            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();


                            tvResult.setText(outputFeature0.getFloatArray()[0] + "\n" + outputFeature0.getFloatArray()[1]);

                            // Releases model resources if no longer used.
                            model.close();
                        } else if (false) {
                            img = Bitmap.createScaledBitmap(img, 28, 28, false);


                            Mnist model = Mnist.newInstance(getApplicationContext());

                            // Creates inputs for reference.
                            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 28, 28, 1}, DataType.FLOAT32);

                            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                            tensorImage.load(img);
                            ByteBuffer byteBuffer = tensorImage.getBuffer();

                            inputFeature0.loadBuffer(byteBuffer);

                            // Runs model inference and gets result.
                            Mnist.Outputs outputs = model.process(inputFeature0);
                            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                            tvResult.setText(outputFeature0.getFloatArray()[0] + "\n" + outputFeature0.getFloatArray()[1]);

                            // Releases model resources if no longer used.
                            model.close();
                        }

                */


                try {
                        img = Bitmap.createScaledBitmap(img, 28, 28, true);
                        Model model = Model.newInstance(getApplicationContext());
                        // Creates inputs for reference.
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 28, 28, 1}, DataType.FLOAT32);
                        ByteBuffer buffer = getByteBuffer(img);

                    /*
                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                    tensorImage.load(img);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();
                    inputFeature0.loadBuffer(byteBuffer);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    img.recycle();
                    ByteBuffer buffer = ByteBuffer.wrap(byteArray);
    */
                        inputFeature0.loadBuffer(buffer);
                        // Runs model inference and gets result.

                        Model.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                        List<String> labels = labelToList();
                        String PredictedValue = GetLabelPredict(outputFeature0.getFloatArray(), labels);


                        FinalString = PredictedValue;

                        if(PredictedValue != labelNotCloth)
                        {
                            int colorInt = getDominantColor(img);
                            String colorName = getColorName(colorInt);
                            String PredictedColor = colorName.substring(6);
                            PredictedColor = PredictedColor.replace("_", " ");
                            FinalString += " " + PredictedColor;
                            search.setVisibility(View.VISIBLE);
                        }

                        tvResult.setText(FinalString);


                        // Releases model resources if no longer used.
                        model.close();

                } catch (IOException | ClassNotFoundException | IllegalAccessException e) {
                    // TODO Handle the exception
                }

                /*
                try {
                    Mnist model = Mnist.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 28, 28, 1}, DataType.FLOAT32);

                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                    tensorImage.load(img);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();


                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    img.recycle();
                    ByteBuffer buffer = ByteBuffer.wrap(byteArray);

                    inputFeature0.loadBuffer(byteBuffer);
                    //inputFeature0.loadBuffer(buffer);

                    // Runs model inference and gets result.
                    Mnist.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    // Releases model resources if no longer used.
                    model.close();

                    tvResult.setText(outputFeature0.getFloatArray()[0] + "\n"+outputFeature0.getFloatArray()[1]);

                } catch (IOException e) {
                    // TODO Handle the exception
                }
                */
                }
            //}
        });

    }

    @Override
    public void onBackPressed()
    {
        counter++;
        if(counter == 2)
            super.onBackPressed();
    }

    //SELECT IMAGE

    /*
    //Fai foto / prendi da galleria
    public void onChooseFile(View v)
    {
        CropImage.activity().start(MainActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode,data );
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK || resultCode == 0) {
                //imgView.setImageURI(data.getData());

                Uri uri = result.getUri();
                try {
                    img = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgView.setImageBitmap(img);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception e = result.getError();
                Toast.makeText(this, "AIUTOOOOOO!: " + e, Toast.LENGTH_SHORT).show();
            }
            backPress = false;
        }
        else{
            backPress = true;
        }


    }

*/

    private void SelectImage(){

        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {
                    //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    imgChanged = false;
                    dialogInterface.dismiss();
                }
            }

        });
        builder.show();
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){
                Bundle bundle = data.getExtras();
                img = (Bitmap) bundle.get("data");
                imgView.setImageBitmap(img);
                btnPredict.setEnabled(true);
                imgChanged = true;
            }else if(requestCode==SELECT_FILE){
                Uri selectedImageUri = data.getData();
                try {
                    img = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgView.setImageBitmap(img);
                btnPredict.setEnabled(true);
                imgChanged = true;
            }
            backPress = false;
        }
        else{
            backPress = true;
        }

        if(tvResult.getText().toString() != "" && imgChanged)
        {
            tvResult.setText("");
            search.setVisibility(View.INVISIBLE);
        }
    }


    //PREDICT

        //IMG

        public ByteBuffer getByteBuffer(Bitmap bitmap)   {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            ByteBuffer mImg = ByteBuffer.allocateDirect(4*w*h);
            mImg.order(ByteOrder.nativeOrder());
            int[] pixels= new int[w*h];
            bitmap.getPixels(pixels,0,w,0,0,w,h);
            //----------image----------
            for(int p:pixels){
                byte tmp;
                int tmp_blue = p & 0xFF;
                int tmp_green = (p >> 8) & 0xFF;
                int tmp_red = (p >> 16) & 0xFF;
                float value = (float) (0.299*tmp_red + 0.587*tmp_green + 0.114*tmp_blue);
                value=255.0f-value;
                value/=255.0f;
                mImg.putFloat(value);
            }
            return mImg;
        }

        //LABEL

        public List<String> labelToList() {
            BufferedReader r;
            List<String> lines = new ArrayList<String>();
            try {
                r = new BufferedReader(
                        new InputStreamReader(getAssets().open("labels.txt")));

                // do reading, usually loop until end of file reading
                String mLine;
                String line;
                while (true) {
                    if ((line = r.readLine()) == null)
                        break;
                    lines.add(line);
                }
            } catch (Exception e) {
                e.printStackTrace(); // file not found
            }
            return lines;
        }

        public String GetLabelPredict(float[] list, List<String> labels) {
            int indexMax = 0;
            float max = Float.MIN_VALUE;

            for(int i = 0; i < list.length; i++) {
                System.out.println(list[i]);
                if(list[i] > max)
                {
                    max = list[i];
                    indexMax = i;
                }
            }

            if(max<3)
                return labelNotCloth;

            return labels.get(indexMax);
        }



        //COLOR

        public static int getDominantColor(Bitmap bitmap) {
            Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
            final int color = newBitmap.getPixel(0, 0);
            newBitmap.recycle();
            String hex = Integer.toHexString(color);
            return color;
        }

        public String getColorName(int hexColorString) throws ClassNotFoundException, IllegalAccessException {
            int [][] colorPoints2 = {{255, 255, 255}, {0, 0, 0}, {255, 0, 0}};
            String [] colorNames2 = {"white", "black", "red"}; // These two arrays have the same length of N

            ArrayList<int[]> colorPoints = new ArrayList<>();
            ArrayList<String> colorNames = new ArrayList<>(); // These two arrays have the same length of N

            Field[] fields = Class.forName(getPackageName()+".R$color").getDeclaredFields();

            for(Field field : fields) {
                String colorName = field.getName();
                if(colorName.startsWith("color_"))
                {
                    colorNames.add(colorName);
                    int colorId = field.getInt(null);
                    int color = getResources().getColor(colorId);
                    int [] colorRGB = getRGB(color);
                    colorPoints.add(colorRGB);
                }
            }


            int [] myColor = getRGB(hexColorString); // Returns int array of length 3


            double minDistance = Double.MAX_VALUE;
            String closestColorName = colorNames.get(0);
            for (int i = 0; i < colorNames.size(); i++){
                int [] color = colorPoints.get(i);
                double d0 = (color[0] - myColor[0]);
                double d1 = (color[1] - myColor[1]);
                double d2 = (color[2] - myColor[2]);
                double distance = Math.sqrt(d0*d0 + d1*d1 + d2*d2);
                if (distance < minDistance){
                    minDistance = distance;
                    closestColorName = colorNames.get(i);
                }
            }

            return closestColorName;

        }

        public static int[] getRGB(final int colorInt) {
            int r = (colorInt & 0xFF0000) >> 16;
            int g = (colorInt & 0xFF00) >> 8;
            int b = (colorInt & 0xFF);
            return new int[] {r, g, b};
        }








    //SEARCH

    // chiama passaggio a nuova activity (Add Activity)
    // aggiungi {android:onClick="openAdd"} in xml del main nelle proprietà del button che apre nuova schermata
    public void openSecondWindow(View v){
        Intent i = new Intent(getApplicationContext(),SearchActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("label", FinalString);
        i.putExtras(bundle);

        setResult(Activity.RESULT_OK, i);

//        SearchActivity cls2 = new SearchActivity();
  //      cls2.activityResultLauncher.launch(i);

        startActivityForResult(i,1);

    }

    @Override
    protected void onRestart() {
        super.onRestart();


        if(backPress && imgChanged)
        {
            imgView.setImageDrawable(null);
            search.setVisibility(View.INVISIBLE);
            tvResult.setText("");
            btnPredict.setEnabled(false);
        }

    }



    //OLD METHOD NOT USED
    public String BitmapToFile(Bitmap bmp) throws IOException {
        // Assume block needs to be inside a Try/Catch block.
        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut = null;
        String fileName = "img.png";
        File file = new File(path,fileName);

        if (file.exists())
        {
            file.delete();
        }

        try {
            file.createNewFile();
            fOut = new FileOutputStream(file,false);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            file.deleteOnExit();
            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public Bitmap ConvertToGrayscale(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        float[] arrayForColorMatrix = new float[] {0, 0, 1, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0};

        Bitmap.Config config = bitmap.getConfig();
        Bitmap grayScaleBitmap = Bitmap.createBitmap(width, height, config);

        Canvas c = new Canvas(grayScaleBitmap);
        Paint paint = new Paint();

        ColorMatrix matrix = new ColorMatrix(arrayForColorMatrix);
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        paint.setColorFilter(filter);

        c.drawBitmap(bitmap, 0, 0, paint);

        return grayScaleBitmap;
    }

    public boolean FieldNotBlank() {
        boolean res = false;
        if( imgView.getDrawable() == null)

        {
            /*
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(200); //You can manage the blinking time with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(2);
            fab.startAnimation(anim);
            btnPredict.setError( "Select an image!" );
             */
            btnPredict.setEnabled(false);
        }

        else {
            res = true;
            //btnPredict.setError(null);
            btnPredict.setEnabled(true);
        }
        return res;
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100)
        {
            imgView.setImageURI(data.getData());

            Uri uri = data.getData();
            try {
                img = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
     */
}
