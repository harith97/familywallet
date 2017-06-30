package ccpe001.familywallet.admin;import android.Manifest;import android.app.ProgressDialog;import android.content.Context;import android.content.DialogInterface;import android.content.Intent;import android.content.pm.PackageManager;import android.content.res.Resources;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.graphics.drawable.BitmapDrawable;import android.graphics.drawable.Drawable;import android.media.Image;import android.net.Uri;import android.os.Build;import android.os.Bundle;import android.provider.MediaStore;import android.support.annotation.NonNull;import android.support.annotation.RequiresApi;import android.support.design.widget.FloatingActionButton;import android.support.v4.app.ActivityCompat;import android.support.v4.content.ContextCompat;import android.support.v4.graphics.drawable.RoundedBitmapDrawable;import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;import android.support.v7.app.AlertDialog;import android.support.v7.app.AppCompatActivity;import android.util.AttributeSet;import android.util.Base64;import android.util.Log;import android.view.View;import android.widget.*;import ccpe001.familywallet.R;import ccpe001.familywallet.Validate;import com.google.android.gms.tasks.OnFailureListener;import com.google.android.gms.tasks.OnSuccessListener;import com.google.firebase.auth.FirebaseAuth;import com.google.firebase.auth.FirebaseUser;import com.google.firebase.database.DatabaseReference;import com.google.firebase.database.FirebaseDatabase;import com.google.firebase.storage.FirebaseStorage;import com.google.firebase.storage.OnProgressListener;import com.google.firebase.storage.StorageReference;import com.google.firebase.storage.UploadTask;import org.apache.poi.hpsf.Constants;import java.io.ByteArrayOutputStream;import java.io.File;import java.io.IOException;import static android.app.Activity.RESULT_OK;public class GetInfo extends AppCompatActivity implements View.OnClickListener{  private FloatingActionButton imageButton;  private Button signUpButton;  private EditText fnameTxt,lnameTxt;  private final static int CAMERA_PERMIT = 0;  private final static int MANAGE_DOC_PERMIT = 1;  private final static int READ_EX_PERMIT = 2;  private static final int RQ_CAPTURE = 4;  private static final int RQ_GALLERY_REQUEST = 5;  private RoundedBitmapDrawable round;  private Uri sendProPicURI;  private DatabaseReference databaseReference;  private StorageReference storageReference;  private FirebaseAuth mAuth;  @Override  protected void onCreate(Bundle savedInstanceState) {    super.onCreate(savedInstanceState);    setContentView(R.layout.get_info);    init();  }  private void init() {    getSupportActionBar().setTitle("Enter details");    mAuth = FirebaseAuth.getInstance();      if(mAuth.getCurrentUser() == null){          finish();          Intent intent = new Intent(this,SignIn.class);          startActivity(intent);      }      databaseReference = FirebaseDatabase.getInstance().getReference();      storageReference = FirebaseStorage.getInstance().getReference();    imageButton = (FloatingActionButton) findViewById(R.id.imageButton);    signUpButton = (Button) findViewById(R.id.signUPGetInfo);    fnameTxt = (EditText) findViewById(R.id.editText);    lnameTxt = (EditText) findViewById(R.id.editText4);    signUpButton.setOnClickListener(this);    imageButton.setOnClickListener(this);    if(!hasCamera()){      imageButton.setEnabled(false);    }  }  @Override  public void onClick(View view) {    if(view.getId()==R.id.imageButton){      String [] optArr ={"Take a Photo","Select from Gallery"};      final AlertDialog.Builder builder = new AlertDialog.Builder(this);      builder.setTitle("Select Option");      builder.setItems(optArr, new DialogInterface.OnClickListener() {        @Override        public void onClick(DialogInterface dialogInterface, int opt) {          if(opt==0){            if (ActivityCompat.checkSelfPermission(GetInfo.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {                ActivityCompat.requestPermissions(GetInfo.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMIT);            }            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);            startActivityForResult(cameraIntent,RQ_CAPTURE);          }else if(opt==1){            if (ActivityCompat.checkSelfPermission(GetInfo.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||                    ActivityCompat.checkSelfPermission(GetInfo.this, Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED) {              ActivityCompat.requestPermissions(GetInfo.this,new String[]{Manifest.permission.MANAGE_DOCUMENTS},MANAGE_DOC_PERMIT);              ActivityCompat.requestPermissions(GetInfo.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_EX_PERMIT);            }            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);            startActivityForResult(galleryIntent,RQ_GALLERY_REQUEST);          }        }      });      AlertDialog alertDialog = builder.create();      alertDialog.show();    }else if(view.getId()==R.id.signUPGetInfo){      if(Validate.ContainOnlyLetters(fnameTxt.getText().toString())) {        if(Validate.ContainOnlyLetters(lnameTxt.getText().toString())) {            Intent intent = new Intent("ccpe001.familywallet.DASHBOARD");            intent.putExtra("firstname",fnameTxt.getText().toString());            intent.putExtra("lastname",lnameTxt.getText().toString());            try {                intent.putExtra("profilepic", sendProPicURI.toString());            }catch (Exception e){            }            saveData();            uploadImg();            startActivity(intent);        }else{          lnameTxt.setError("Invalid last name");        }      }else {        fnameTxt.setError("Invalid first name");      }    }  }    private void saveData() {        UserData userData = new UserData(fnameTxt.getText().toString().trim(),                                            lnameTxt.getText().toString().trim());        FirebaseUser firebaseUser = mAuth.getCurrentUser();        databaseReference.child("UserInfo").setValue(userData);    }    private void uploadImg(){        if(sendProPicURI != null) {            final ProgressDialog progressDialog = new ProgressDialog(this);            progressDialog.setTitle("Setting up..");            progressDialog.show();            StorageReference reference = storageReference.child("images/"+fnameTxt.getText().toString().trim()                    +"_"+lnameTxt.getText().toString().trim()+".jpg");            reference.putFile(sendProPicURI)                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {                        @Override                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {                            progressDialog.dismiss();                        }                    }).addOnFailureListener(new OnFailureListener() {                @Override                public void onFailure(@NonNull Exception e) {                    progressDialog.dismiss();                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();                }            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {                @Override                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {                    double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();                    progressDialog.setMessage((int)progress+"% Uploaded..");                }            });        }else{            Toast.makeText(this,"File not selected",Toast.LENGTH_SHORT).show();        }    }    private boolean hasCamera() {    return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);  }  @Override  protected void onActivityResult(int requestCode, int resultCode, Intent data) {    if(requestCode==RQ_GALLERY_REQUEST&&resultCode==RESULT_OK){      sendProPicURI = data.getData();      try {        round = RoundedBitmapDrawableFactory.create(getResources(), MediaStore.Images.Media.getBitmap(this.getContentResolver(),sendProPicURI));      } catch (IOException e) {        e.printStackTrace();      }      round.setCircular(true);      imageButton.setImageDrawable(round);    }else if(requestCode==RQ_CAPTURE&&resultCode==RESULT_OK){        Bitmap photo = (Bitmap) data.getExtras().get("data");        sendProPicURI = getImageUri(getApplicationContext(),photo);        round = RoundedBitmapDrawableFactory.create(getResources(), photo);        round.setCircular(true);        imageButton.setImageDrawable(round);    }  }    private Uri getImageUri(Context inContext, Bitmap inImage) {        ByteArrayOutputStream bytes = new ByteArrayOutputStream();        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);        return Uri.parse(path);    }}