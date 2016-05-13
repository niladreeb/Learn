package gojimo.app.learn;

import android.test.ActivityInstrumentationTestCase2;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.robotium.solo.Solo;



/**
 * Created by niladreeb on 12/05/2016.
 */

public class RobotiumTest extends ActivityInstrumentationTestCase2<MainActivity> {

        private Solo solo;

        public RobotiumTest() {
            super(MainActivity.class);

        }

        @Override
        public void setUp() throws Exception {
            //setUp() is run before a test case is started.
            //This is where the solo object is created.
            setActivityInitialTouchMode(true);
            solo = new Solo(getInstrumentation(), getActivity());
        }

        @Override
        public void tearDown() throws Exception {
            //tearDown() is run after a test case has finished.
            //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
            solo.finishOpenedActivities();
        }

        public void testFirstRun() throws Exception {
            //Unlock the lock screen
            solo.unlockScreen();

            //Assert that Main Activity is opened
            solo.assertCurrentActivity("Expected MainActivity", "MainActivity");

                View view = solo.getView(R.id.top_layout);
                if(view.getVisibility()==View.VISIBLE) {
                    solo.clickOnView(view);
                    assertEquals(view.getVisibility(),View.INVISIBLE);
                }

            //Scenario 1
            solo.clickOnText("SAT");
            //Assert that Subject Activity is opened
            solo.assertCurrentActivity("Expected SubjectActivity", "SubjectActivity");
            boolean subjectFound = solo.searchText("SAT Critical Reading");
            assertTrue(subjectFound);
            //Go back
            solo.goBack();
            //Assert that Main Activity is opened
            solo.assertCurrentActivity("Expected MainActivity", "MainActivity");

            //Scenario 2
            solo.clickOnText("ASVAB");
            //Assert that Subject Activity is opened
            solo.assertCurrentActivity("Expected SubjectActivity", "SubjectActivity");
            boolean subjectNotFound1 = solo.searchText("Subjects Unavailable");
            assertTrue(subjectNotFound1);
            //Click on OK
            solo.clickOnButton("OK");
            //Assert that Product Activity is opened
            solo.assertCurrentActivity("Expected ProductActivity", "ProductActivity");
            boolean referenceFound = solo.searchText("McGraw Hill's ASVAB Test Prep Sample");
            assertTrue(referenceFound);
            solo.goBack();
            //Assert that Main Activity is opened
            solo.assertCurrentActivity("Expected MainActivity", "MainActivity");


            //Scenario 3
            solo.clickOnText("Test");
            //Assert that Subject Activity is opened
            solo.assertCurrentActivity("Expected SubjectActivity", "SubjectActivity");
            boolean subjectNotFound2 = solo.searchText("Subjects Unavailable");
            assertTrue(subjectNotFound2);
            //Click on OK
            solo.clickOnButton("OK");
            //Assert that Product Activity is opened
            solo.assertCurrentActivity("Expected ProductActivity", "ProductActivity");
            boolean referenceNotFound = solo.searchText("References Unavailable");
            assertTrue(referenceNotFound);
            solo.clickOnButton("OK");
            //Assert that Main Activity is opened
            solo.assertCurrentActivity("Expected MainActivity", "MainActivity");
        }


    }