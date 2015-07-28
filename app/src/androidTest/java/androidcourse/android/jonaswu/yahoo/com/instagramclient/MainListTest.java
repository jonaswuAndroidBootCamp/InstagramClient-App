package androidcourse.android.jonaswu.yahoo.com.instagramclient;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by jonaswu on 2015/7/28.
 */
public class MainListTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "androidcourse.android.jonaswu.yahoo.com.instagramclient.MainActivity";

    private static Class<?> launcherActivityClass;

    static {
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public MainListTest() throws ClassNotFoundException {
        super(launcherActivityClass);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testRun() {
        //Wait for activity: 'com.example.ExampleActivty'
        solo.waitForActivity("MainActivity", 2000);
        solo.assertCurrentActivity("MainActivity", MainActivity.class);
        solo.clickInList(0);
        //Clear the EditText editText1
    }
}
