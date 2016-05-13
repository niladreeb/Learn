# Learn
A functional demo app which implemented Splash screen, ListView, REST API Integration using JSON, SwipeRefreshLayout, Internal Storage, ViewOverlay and Robotium test case.


<h5>1. This app calls the Gojimo api and displays various qualifications and the subjects required for it. If subjects are unavailable it shows reference books.</h5>
<h5>2. Shows a crisp splash screen with animation.</h5>
<h5>3. Calls the api and shows all the qualifications in a list view.</h5>
<h5>4. On click of the qualifications(i.e list view items) it navigates to the next subject activity. If subjects are available it shows them with their respective colours. In case of no colour a default colour is set.</h5>
<h5>5. If subjects are unavailable it shows a dialog and asks if the user want to see some reference books. If yes it moves to the next product activity and shows the reference books, if available.</h5>
<h5>6. A view overlay is displayed to display working of the app when the app is launched for the first time.</h5>
<h5>7. Drag to refresh mechanism is used to refresh when only requested. So the app uses existing data until new data is asked for.</h5>
<h5>8. The app stores the entire json in the internal storage and always loads from it later unless the app is refreshed. Due to internal storage the contents are safe and gets deleted when the app is uninstalled.</h5>
<h5>9. Robotium test case is included for the app to test the entire work flow. Kindly run RobotiumTest to start the automated testing.</h5>
<h5>10. Attached some screenshots of the app for reference.
