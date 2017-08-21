##	Library Details
Version	1.0.0
Packaging	Android Module/Library

##	Integration & Initialization
Import the “dataengine” module to project folder.
Add the module to gradle dependency and sync the project
The library initialization should be done in Application class

DataEngine.with(context).init();
		
Usage example:
public class AppCls extends Application implements AppStateListener {

@Override
public void onCreate() {
	super.onCreate();
	DataEngine.with(this).setAutoScreenCapture(true).setAppStateMonitor(this).doCaptureCrash().init();
	DataEngine.with(this).identifyEvent();
}


@Override
public void onAppDidEnterForeground() {
	Log.e("APPSTATE","onAppDidEnterForeground");

}

@Override
public void onAppDidEnterBackground() {
	Log.e("APPSTATE","onAppDidEnterBackground");
}
}		



