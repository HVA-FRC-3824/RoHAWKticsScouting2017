package frc3824.rohawkticsscouting2017.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author Andrew Messing
 *         Created: 8/22/16
 */
public class Authentication {

    private final static String TAG = "Authentication";

    private FirebaseAuth mFirebaseAuth;

    private static Authentication mAuthentication;
    private boolean mAuthenticated;

    public static Authentication getInstance() {
        if(mAuthentication == null)
        {
            mAuthentication = new Authentication();
        }
        return mAuthentication;
    }

    private Authentication() {
        mAuthenticated = false;
        mFirebaseAuth = FirebaseAuth.getInstance();
        authenticate();
    }

    public boolean isAuthenticated()
    {
        return mAuthenticated;
    }

    public void authenticate() {
        mFirebaseAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, authResult.getUser().getUid());
                mAuthenticated = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
                mAuthenticated = false;
            }
        });
    }

}
