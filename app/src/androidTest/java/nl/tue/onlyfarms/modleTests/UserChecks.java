package nl.tue.onlyfarms.modleTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.testing.TestLifecycleOwner;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import nl.tue.onlyfarms.model.FirebaseUserService;
import nl.tue.onlyfarms.model.OurFirebaseDatabase;
import nl.tue.onlyfarms.model.User;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserChecks {

    // makes it so the livedata can be observed
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void create_and_query_user() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(appContext);
//        OurFirebaseDatabase.USE_EMULATOR = true;
        // creates a user and adds it to the database
        User user = new User();
        user.setUid("test_uid");
        user.setUserName("T.Tester");
        user.setFirstName("Testy");
        user.setLastName("Tester");
        user.setEmailAddress("tester@testing.com");
        user.setStatus(User.Status.VENDOR);
        FirebaseUserService.updateUser(user);
        MutableLiveData<User> result;
        result = FirebaseUserService.getUser("test_uid");
        result.observe(new TestLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (result.getValue() == null) {
                    // this would always be false
                    assertNotEquals(null, result.getValue());
                }
                else {
                    // this is always true
                    assertNotEquals(null, result.getValue());
                    // check if a user is returned
                    assertEquals(User.class, result.getValue().getClass());
                    User userTest = result.getValue();
                    assertEquals("test_uid", userTest.getUid());
                    assertEquals("T.Tester", userTest.getUserName());
                    assertEquals("tester@testing.com", userTest.getEmailAddress());
                    assertEquals("Testy", userTest.getFirstName());
                    assertEquals("Tester", userTest.getLastName());
                    assertEquals(User.Status.VENDOR, userTest.getStatus());
                }
            }
        });


        // here so that we know the observer code ran
        assertNotNull(result.getValue());

        FirebaseUserService.deleteUser(user);
    }
}