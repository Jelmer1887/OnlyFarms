package nl.tue.onlyfarms.modelTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.testing.TestLifecycleOwner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.FirebaseApp;

import org.junit.Rule;
import org.junit.Test;

import nl.tue.onlyfarms.model.FireBaseService;
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

    @Test(timeout = 2000)
    public void create_and_query_user() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(appContext);
        // OurFirebaseDatabase.USE_EMULATOR = true;

        FireBaseService<User> userService = new FireBaseService<>(User.class, "users");

        // creates a user and adds it to the database
        User user = new User();
        user.setUid("test_uid");
        user.setUserName("T.Tester");
        user.setFirstName("Testy");
        user.setLastName("Tester");
        user.setEmailAddress("tester@testing.com");
        user.setStatus(User.Status.VENDOR);
        userService.updateToDatabase(user, user.getUid());
        MutableLiveData<User> result = userService.getFirstResult();
        userService.getFirstMatchingField("uid", user.getUid());
        result.observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (result.getValue() == null) {
                    // this should always be false
                    assertNotEquals(null, result.getValue());
                }
                else {
                    System.out.println("Observer ran");
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

        // gives the result time to update
        User value = result.getValue();

        while(value == null) {
            value = result.getValue();
        }

        // here so that we know the observer code ran
        assertNotNull(result.getValue());

        userService.deleteFromDatabase(user.getUid());
    }

    @Test(timeout = 2000)
    public void delete_user() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(appContext);
        // OurFirebaseDatabase.USE_EMULATOR = true;

        FireBaseService<User> userService = new FireBaseService<>(User.class, "users");

        // creates a user and adds it to the database
        User user = new User();
        user.setUid("test_uid");
        user.setUserName("T.Tester");
        user.setFirstName("Testy");
        user.setLastName("Tester");
        user.setEmailAddress("tester@testing.com");
        user.setStatus(User.Status.VENDOR);
        userService.updateToDatabase(user, user.getUid());
        MutableLiveData<User> result = userService.getFirstResult();
        userService.getFirstMatchingField("uid", user.getUid());
        // apparently the data isn't updated if it isn't observed
        result.observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {}
        });


        // gives the result time to update
        User value = result.getValue();

        while(value == null) {
            value = result.getValue();
        }

        // Make sure the user is not null
        assertNotNull(result.getValue());

        // actually delete it
        userService.deleteFromDatabase(user.getUid());

        while (value != null) {
            value = result.getValue();
        }

        // check if deleting the user makes it null
        assertNull(result.getValue());

        // note: the asserts here will always return true
        // instead the testcase fail is signified by the timeout
    }
}