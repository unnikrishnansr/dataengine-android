## Library Details
|Version	|1.0.0|
|-------|-------|
|Packaging|	Android Module/Library|

## Integration & Initialization
Import the “dataengine” module to project folder.
Add the module to gradle dependency and sync the project
The library initialization should be done in Application class

```sh
DataEngine.with(context).init();
```
		
Usage example:

```sh
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

```	



## Additional customization options

| Method | Description |
| ------ | ------ |
|setServerURL(String url)|	To set custom analytics URL. If no value is supplied then the default built-in URL will be selected.|
|doCaptureCrash()|	Calling this method will enable crash reporting service.|
|setAppStateMonitor(AppStateListener stateListener)|	Sets the callback for application state monitor. The callbacks defined are onAppDidEnterForeground() and onAppDidEnterBackground()|
|setAutoScreenCapture()|	Enables the option for auto-screen capture functionality|


Usage example: 
```sh 
DataEngine.with(context).setAutoScreenCapture(true).setAppStateMonitor(this).doCaptureCrash().init();
```

## Built-in event tracking functions
The following events can be accessed using 

```sh
DataEngine.getInstance()
```

### Identify
#### identifyEvent(): 
The method is used to associate the user and session with rest of the tracking events. This can be used for non-logged users where the temporary user id is created using base64 encoded value created using device id and application package name. The method invokes both identify and session events simultaneously.

Usage example:
```sh
DataEngine.getInstance().identifyEvent();
```

#### identifyEvent(String uid, BaseEventMap map): 
The method allows users to configure the actual user id and other user info using BaseEventMap. Usually called after login or registration. The method invokes both identify and session events simultaneously.


Usage example:

```sh
BaseEventMap map=new BaseEventMap();
map.putValue("First Name", "John");
map.putValue("Last Name", "Arc");
map.putValue("Email", "john@mail.com");

DataEngine.getInstance().identifyEvent("2221", map);
```


### Session
The method is called each time whenever a new session is required to be initiated.

Usage example: 
```sh
DataEngine.getInstance().sessionStart();
```


### Screen
#### screenEvent(String screenName, BaseEventMap map): 
The screen method allows users to record whenever a user navigates to different screen of the application, along with additonal information about the page being visited. 

Usage example:
```sh
BaseEventMap map=new BaseEventMap();
map.putValue("ContentId", "213442218882273");
map.putValue("Title", "Sully");
map.putValue("Genere", "Action");

DataEngine.getInstance().screenEvent("details_page", map);
```

### Track
#### trackEvent(String action, String sourceName, BaseEventMap map):
The track method allows users to record the actions of the users. Every triggered action have associated properties.

Usage example:
```sh
BaseEventMap map=new BaseEventMap();
map.putValue("ContentId", "213442218882273");
map.putValue("Title", "Sully");
map.putValue("Genere", "Action");

DataEngine.getInstance().trackEvent("playClick", "details_page", map);	
```

## Stop analytics service
Use the following method to stop the service

```sh
shutdown()
```

Usage example:

```sh
DataEngine.with(context).shutdown();
```



