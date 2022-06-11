package com.example.project1.virtualinteriordesign;

import static com.google.ar.sceneform.rendering.ModelRenderable.builder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.virtualinteriordesign.adapter.CategoryAdapter;
import com.example.project1.virtualinteriordesign.model.GalleryModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static String SELECTED_MODEL_LINK = "";
//    https://firebasestorage.googleapis.com/v0/b/virtual-interior-design-61c13.appspot.com/o/Objects%2FChair%2FLCW1.glb?alt=media&token=a44d1ebb-b83a-4718-91c0-52bea17aa53c
    private ArFragment arCam;
    private boolean textingLock = false;

    AnchorNode currentSelectedAnchorNode = null;
    TransformableNode currentSelectedTransNode = null;
    ArrayList<TransformableNode> lockedNode;
    private TextView count, profilePic;
    //    private int object = R.raw.chair;
    private String objectName = "Chair";
    private LinearLayout hiddenOpt, hiddenList;
    private ImageView moreOpt, showList, lockObj, deleteBtn, saveBtn, shareBtn;
    RecyclerView recyclerView;
    CategoryAdapter adapter;
    ArrayList<String> cat;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){

            firebaseDatabase=FirebaseDatabase.getInstance();
            //hooks
            hiddenOpt = findViewById(R.id.hiddenOpt);
            hiddenList = findViewById(R.id.hiddenList);
            moreOpt = findViewById(R.id.moreLessIcon);
            lockObj = findViewById(R.id.FixObject);
            showList = findViewById(R.id.showList);
            recyclerView = findViewById(R.id.listModel);
            profilePic = findViewById(R.id.UserProfile);
            saveBtn = findViewById(R.id.Save);
            shareBtn = findViewById(R.id.Share);
            deleteBtn = findViewById(R.id.deleteBtn);
            lockedNode = new ArrayList<TransformableNode>();

            showList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moreOptions(showList, hiddenList);
                }
            });
            moreOpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moreOptions(moreOpt, hiddenOpt);
                }
            });

            lockObj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lockedNode.contains(currentSelectedTransNode)){
                        lockedNode.remove(currentSelectedTransNode);
                        currentSelectedTransNode.getTranslationController().setEnabled(true);
                        Toast.makeText(getApplicationContext(), "Item Unlocked", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        lockedNode.add(currentSelectedTransNode);
                        currentSelectedTransNode.getTranslationController().setEnabled(false);
                        Toast.makeText(getApplicationContext(), "Item Locked", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//
                }
            });

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto();
                }
            });


            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                if (currentSelectedAnchorNode !=null){
//                    arCam.getArSceneView().getScene().removeChild(currentSelectedAnchorNode);
//                    currentSelectedAnchorNode.getAnchor().detach();
//                    currentSelectedAnchorNode.setParent(null);
//                    currentSelectedAnchorNode = null;
//                    Toast.makeText(getApplicationContext(), "Item removed successfully", Toast.LENGTH_SHORT).show();
//                }
                    deleteNode(currentSelectedTransNode);
                }
            });



//        Firebase auth

            Intent intent=getIntent();
            String userAuth = FirebaseAuth.getInstance().getUid();

            DatabaseReference ref = firebaseDatabase.getReference().child("user").child(userAuth);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("===============",snapshot.child("username").getValue().toString());
                    String UserName = snapshot.child("username").getValue().toString();
                    String Email = snapshot.child("email").getValue().toString();
                    Log.d("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\",userAuth+"   -=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-    "+UserName);
                    profilePic.setText(UserName.substring(0, 1).toUpperCase());
                    profilePic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Firebase auth/
                            Intent intent = new Intent(getApplicationContext(), Profile.class);
                            intent.putExtra("authId", userAuth);
                            intent.putExtra("userName", UserName);
                            intent.putExtra("emaiul",Email);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




            fetchCat();
            adapter = new CategoryAdapter(this, cat);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            String modelLink = SELECTED_MODEL_LINK;
            //geeks for geeks
            arCam = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arCameraArea);
            arCam.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
                @Override
                public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                    if(SELECTED_MODEL_LINK.isEmpty()){
                        //default
                        Anchor anchor = hitResult.createAnchor();
                        builder()
                                .setSource(getApplicationContext(), R.raw.chair_2)
                                .setIsFilamentGltf(true)
                                .build()
                                .thenAccept(modelRenderable -> addModel(anchor, modelRenderable))
                                .exceptionally(throwable -> {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                    builder.setMessage("Something went Wrong");
                                    return null;
                                });
                    }
                    else{
                        Anchor anchor = hitResult.createAnchor();
                        builder()
                                .setSource(getApplicationContext(), Uri.parse(SELECTED_MODEL_LINK))
                                .setIsFilamentGltf(true)
                                .build()
                                .thenAccept(modelRenderable -> addModel(anchor, modelRenderable))
                                .exceptionally(throwable -> {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                    builder.setMessage("Something went Wrong");
                                    return null;
                                });
                    }
                }
            });

            arCam.getArSceneView().getScene().addOnPeekTouchListener(new Scene.OnPeekTouchListener() {
                @Override
                public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                    if (hitTestResult.getNode() != null) {
                        currentSelectedTransNode = (TransformableNode) hitTestResult.getNode();
                    }
                }
            });
//        arCam.setOnTapPlaneGlbModel(modelLink);


        }
        else{
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }



    //start of Save File
    private String generateFilename() {


        String date = new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault()).format(new Date());
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + "VID/" + date + ".jpg";
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {

        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputData);
            //start firebase upload
            byte[] data = outputData.toByteArray();
            StorageReference imageStorage = FirebaseStorage.getInstance().getReference();
            //bhai yaha user id likh dena
            String authId = "demoId";
            StorageReference imageRef = imageStorage.child("images/" + authId + "/" + filename);

            Task<Uri> urlTask = imageRef.putBytes(data).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String uri = downloadUri.toString();
//
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("user").child("bhai yaha pr bhi auth id likh na")
                            .child(filename);
                    GalleryModel galleryModel = new GalleryModel(uri, filename);
                    ref.setValue(galleryModel);
                } else {
                    // Handle failures
                    // ...
                }
            });
            //end of fire base upload
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }

    private void takePhoto() {

        final String filename = generateFilename();
        ArSceneView view = arCam.getArSceneView();

        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            PixelCopy.request(view, bitmap, new PixelCopy.OnPixelCopyFinishedListener() {
                @Override
                public void onPixelCopyFinished(int copyResult) {
                    if (copyResult == PixelCopy.SUCCESS) {
                        try {
                            saveBitmapToDisk(bitmap, filename);
                            Uri uri = Uri.parse("file://" + filename);
                            Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            i.setData(uri);
                            sendBroadcast(i);
                        } catch (IOException e) {
                            Toast toast = Toast.makeText(getApplicationContext(), e.toString(),
                                    Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                "SnapShot Saved to device", Snackbar.LENGTH_LONG);
                        snackbar.setAction("View", v -> {
                            File photoFile = new File(filename);

                            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                    getApplicationContext().getPackageName() + ".ar.codelab.name.provider",
                                    photoFile);
                            Intent intent = new Intent(Intent.ACTION_VIEW, photoURI);
                            intent.setDataAndType(photoURI, "image/*");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                        });
                        snackbar.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Something went wrong" + copyResult, Toast.LENGTH_LONG);
                        toast.show();
                    }
                    handlerThread.quitSafely();
                }

            }, new Handler(handlerThread.getLooper()));
        }
    }

    //end of Save File




    private void deleteNode(TransformableNode node) {
        if (node != null) {
            arCam.getArSceneView().getScene().removeChild(node);
            if(lockedNode.contains(node)){
                lockedNode.remove(node);
            }

//                    trCSNode.getParentNode().getAnchor().detach();
            node.setParent(null);
            node = null;
            Toast.makeText(getApplicationContext(), "Item removed successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCat() {
        cat = new ArrayList<>();
//        cat.add("Sofa");
//        cat.add("Chair");
//        cat.add("Bed");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Objects")
                .child("Category");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String CategoryName = dataSnapshot.child("Name").getValue().toString();
                    cat.add(CategoryName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void moreOptions(ImageView clicked, LinearLayout hidden) {
        if (hidden.getVisibility() == View.GONE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition((ViewGroup) hidden.getParent(), new AutoTransition());
            }
            hidden.setVisibility(View.VISIBLE);
            clicked.setRotation(180f);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition((ViewGroup) hidden.getParent(), new AutoTransition());
            }
            hidden.setVisibility(View.GONE);
            clicked.setRotation(0f);
        }
    }

    private void addModel(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
//        currentSelectedAnchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arCam.getArSceneView().getScene());
        TransformableNode transformableNode = new TransformableNode(arCam.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        transformableNode.select();
    }


}