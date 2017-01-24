package com.groupin.florianmalapel.groupin.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.tools.DLog;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import java.io.ByteArrayOutputStream;

/**
 * Created by florianmalapel on 28/10/2016.
 */

public class GICommunicationsHelper {
    private static final String STORAGE_REF = "gs://groupin-146311.appspot.com";
    private final String TAG = "GICommsHelper";
    private FirebaseAuth firebaseAuth = null;
    private Context context = null;
    private FirebaseAuth.AuthStateListener mAuthListener = null;
    private GIVolleyHandler volleyHandler = null;
    private GISharedPreferencesHelper prefsHelper = null;

    public GICommunicationsHelper(Context _context) {
        firebaseAuth = FirebaseAuth.getInstance();
        context = _context;
        volleyHandler = new GIVolleyHandler();
        prefsHelper = new GISharedPreferencesHelper(context);
    }


    public interface FirebaseGoogleLoginSuccess {
        void firebaseGoogleLoginSuccess();
    }

    public interface FirebaseFacebookLoginSuccess {
        void firebaseFacebookLoginSuccess();
    }

    public interface FirebaseCreateUserCallback {
        void firebaseCreateUserSuccess(Task task);
        void firebaseCreateUserFailed(Task task);
    }

    public interface FirebaseUploadImageCallback {
        void firebaseUploadSuccess(String url);
        void firebaseUploadFailed();
    }
    /**
     * Create a user on firebase with an email and a password
     *
     * @param email    - email of the user
     * @param password - password of the user
     */
    public void createFirebaseUserWithEmailAndPassword(String email, String password, final FirebaseCreateUserCallback firebaseCreateUserCallback, final GIVolleyRequest.RequestCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            GIApplicationDelegate.getInstance().getDataCache().user.setDataFromFirebase(task.getResult().getUser());
                            volleyHandler.postUser(GIApplicationDelegate.getInstance().getDataCache().user, callback);
                            firebaseCreateUserCallback.firebaseCreateUserSuccess(task);
                        } else {
                            Log.v("))- GIComms", task.getException().toString());
                            firebaseCreateUserCallback.firebaseCreateUserFailed(task);
                        }
                    }
                });
    }

    public void signInWithEmailAndPassword(String email, String password, final FirebaseCreateUserCallback firebaseCreateUserCallback, final GIVolleyRequest.RequestCallback callback){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            GIApplicationDelegate.getInstance().getDataCache().user.setDataFromFirebase(task.getResult().getUser());
                            volleyHandler.postUser(GIApplicationDelegate.getInstance().getDataCache().user, callback);
                            firebaseCreateUserCallback.firebaseCreateUserSuccess(task);
                        } else {
                            Log.v("))- GIComms", task.getException().toString());
                            firebaseCreateUserCallback.firebaseCreateUserFailed(task);
                        }
                    }
                });

    }



    public void firebaseAuthWithFacebook(String token, AppCompatActivity activity, final FirebaseFacebookLoginSuccess loginSuccess, final GIVolleyRequest.RequestCallback callback){
        prefsHelper.storeUserAccessTokenFb(token);
        DLog.logwtf(TAG, "facebook is signing");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    // User is signed out
                }
            }
        };

        AuthCredential credential = FacebookAuthProvider.getCredential(token);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.wtf(TAG, "signInWithCredential", task.getException());
                        }

                        else {
                            GIApplicationDelegate.getInstance().getDataCache().user.setDataFromFirebase(task.getResult().getUser());
                            loginSuccess.firebaseFacebookLoginSuccess();
                            volleyHandler.postUser(GIApplicationDelegate.getInstance().getDataCache().user, callback);
                        }
                    }
                });
    }

    public void firebaseAuthWithGoogle(String idToken, AppCompatActivity activity, final FirebaseGoogleLoginSuccess googleLoginSuccess, final GIVolleyRequest.RequestCallback callback) {
        prefsHelper.storeUserIdTokenGoogle(idToken);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    DLog.logwtf(TAG, "google user is signed");
                } else {
                    // User is signed out
                }
            }
        };

        firebaseAuth.addAuthStateListener(mAuthListener);

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            GIApplicationDelegate.getInstance().getDataCache().user.setDataFromFirebase(task.getResult().getUser());
                            volleyHandler.postUser(GIApplicationDelegate.getInstance().getDataCache().user, callback);
                            DLog.logwtf(TAG, "google log success");
                            googleLoginSuccess.firebaseGoogleLoginSuccess();
                        }
                    }
                });
    }


    public void sendEmailResetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // SUCCESSFUL
                        if (task.isSuccessful()) {
                            DLog.logwtf(TAG, "reset success");
                        } else {
                            DLog.logwtf(TAG, "reset failed: " + task.getException());
                        }

                    }
                });
    }


    public boolean checkIfUserIsLoggedIn() {
        // If user is logged
        if (firebaseAuth.getCurrentUser() != null) {
            return true;
        } else return false;
    }


    public void signOut() {
        if (checkIfUserIsLoggedIn())
            firebaseAuth.signOut();
    }

    public void stopAuthListerner(){
        firebaseAuth.removeAuthStateListener(mAuthListener);
        mAuthListener = null;
    }

    public static void firebaseUploadBitmap(String title, Bitmap bitmap, final FirebaseUploadImageCallback callback){
        StorageReference storageRef = GIApplicationDelegate.getInstance().getFirebaseStorage().getReferenceFromUrl(STORAGE_REF);
        StorageReference imageRef = storageRef.child(title);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                callback.firebaseUploadFailed();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                callback.firebaseUploadSuccess(taskSnapshot.getDownloadUrl().toString());
            }
        });
    }


}
