package views.util
{
	import flash.events.TimerEvent;
	import flash.system.Security;
	import flash.utils.Timer;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.managers.CursorManager;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.mxml.HTTPService;
	
	import views.util.DialogSessionTimeoutWarning;
	
	public class SessionTimeoutData
	{
		public static var sessionCheckTimerInterval:int = 10000; //10 seconds
		public static var seconds_to_display_timeout_warning : int = 900; // seconds
		public static var sessionTimer:Timer;
		public static var sessionMaxInActiveTime:int = -1;
		private static var checkServerSessionMaxInActiveTime:Boolean = false;
		
		[Bindable]
		public static var flexApplication:gnomexFlex;
		
		public function SessionTimeoutData()
		{
		}
		
		public static function setThisApplication(ap:gnomexFlex, sessionTimeout:int):void {
			flexApplication = ap;
			// Subtract a minute from the systemTimeout just to make sure timeout doesn't
			// happen before the countdown is finished.
			sessionMaxInActiveTime = sessionTimeout - 60;
		}
		
		public static function getThisApplication():Application {
			return flexApplication;
		}
		
		/**
		 * start session timer to get inactive time from this session 
		 * user will be warned when there is only 60 seconds left and he/she 
		 * has a choice to make to continue his/her work.
		 */		
		public static function startUserSessionTimer():void{
			//AppControls.sessionTimerInterval = interval;
			if (sessionMaxInActiveTime <= 0){
				// Just in case, default is 1 hour
				sessionMaxInActiveTime = 600 * 6; // 1 hour
			}
			
			//if session timer started already, return
			if (sessionTimer != null) return;
			sessionTimer = new Timer(sessionCheckTimerInterval);
			sessionTimer.addEventListener(TimerEvent.TIMER, checkSessionTimeout);
			sessionTimer.start();	
		}
		
		/**
		 * Stop the session timer -- for cleaning purpose 
		 * 
		 */		
		public static function stopUserSessionTimer():void{
			if(sessionTimer != null){
				sessionTimer.stop();
				sessionTimer = null;	
				sessionMaxInActiveTime = -1;
			}
		}
		
		/**
		 * called by the session timer each time the session timer interval is 
		 * reached. 
		 *  
		 * @param event A timer event
		 * 
		 */		
		public static function checkSessionTimeout(event:TimerEvent):void{
			var service:HTTPService = createHTTPService("CheckSessionStatus.gx");		
			service.addEventListener(ResultEvent.RESULT, handleCheckSessionStatus); 
			service.addEventListener(FaultEvent.FAULT, handleCheckSessionStatusFault);
			var param:Object = new Object();
			param["timeStamp"] = new Date().getMilliseconds();				
			service.send(param);
		}
		
		
		public static function  handleFault(event:FaultEvent):int{
			onFailHttpRequest('Unable to check session status', event);
			return -1;
		}
		
		public static function onFailHttpRequest(title:String, event:FaultEvent):void {
			var sandboxMessage:String = "";
			switch (Security.sandboxType) {
				case Security.LOCAL_TRUSTED:
					sandboxMessage += " (Local trusted)";
					break;
				case Security.LOCAL_WITH_FILE:
					sandboxMessage += " (Local with file)";
					break;
				case Security.LOCAL_WITH_NETWORK:
					sandboxMessage += " (Local with network)";
					break;
				case Security.REMOTE:
					sandboxMessage += " (Remote)";
					break;
			}
			Alert.show("SandboxType=" + sandboxMessage + "\n" + event.fault.toString(), title );
		}
		
		public static function createHTTPService(command:String):HTTPService{
			var service:HTTPService = new  mx.rpc.http.mxml.HTTPService(); 
			if (command != null){
				service.url = command;
			}
			service.useProxy = false;
			service.resultFormat = "e4x";
			service.method = "POST"; 
			if (command.indexOf("KeepHttpSession")<0)
				service.addEventListener("fault", handleFault);
			return service;
		}	
		
		private static function handleCheckSessionStatusFault(event:FaultEvent):void{
			handleFault(event);
		}
		
		/**
		 * Handle the result of the http service CheckSessionStatus
		 * @param event
		 * 
		 */		
		public static function handleCheckSessionStatus(event:ResultEvent):void{
			var error:String = getServerErrorMessage(event);
			if (error == null || error.length <= 0){
				var resultXml:XML = event.result as XML;
				var lastAccessTime:int = parseInt(resultXml.sa.attribute("lastAccessedTime"));
				var currentTime:int = parseInt(resultXml.sa.attribute("currentTime"));
				if (checkServerSessionMaxInActiveTime){
					checkServerSessionMaxInActiveTime = false;
					var maxInActiveTime: int = parseInt(resultXml.sa.attribute("sessionMaxInActiveTime"));
					sessionMaxInActiveTime = maxInActiveTime;
				} 
				//if the inactive time is within 15 seconds of the max inactive time, warn user
				var inactiveTime : int =  int((currentTime -  lastAccessTime)/1000); //seconds; 
				var timeLeft : int = sessionMaxInActiveTime - inactiveTime;
				if (timeLeft <= seconds_to_display_timeout_warning){
					displaySessionTimeOutWarning(seconds_to_display_timeout_warning);	
				}
			} else {
				if(sessionTimer != null) {
					sessionTimer.stop();
				}			
			}
		}
		
		/**
		 * Pops up the time out warning dialog when the remaining time is
		 * less than 1 minute. 
		 * @param timeLeft
		 * 
		 */		
		private static function displaySessionTimeOutWarning(timeLeft:int):void{
			if(sessionTimer != null){
				sessionTimer.stop();
				sessionTimer = null;
			}		
			var timeOutWarning : DialogSessionTimeoutWarning = 
				DialogSessionTimeoutWarning(PopUpManager.createPopUp(getThisApplication(), 
					DialogSessionTimeoutWarning, true));
			timeOutWarning.visible = true;
			timeOutWarning.startSessionCountDown(timeLeft);
			PopUpManager.centerPopUp(timeOutWarning); 		
		}
		
		public static function getServerErrorMessage(event:ResultEvent):String{
			var errorMessage:String = "";
			try{
				if(event == null){
					//Alert.show("Server returned (event)result for your request is null");
					errorMessage = "Server returned (event)result for your request is null";
				}
				else{
					var results:XML = event.result as XML;
					if(results == null){
						errorMessage = "Server returned result from event for your request is null";
					}else{
						var msg:String = results.descendants("ERROR").attribute("message");
						if(msg != null && msg.length > 0){
							//AppLogger.writeLog("Server Message: " + msg);
							errorMessage = errorMessage + msg;
						}
					}
				}
			}catch(error:Error){
				errorMessage = errorMessage +"Error 1: " + event.result.toString();
				errorMessage = errorMessage +"Error 2: " + error.message;
			}
			if (errorMessage.length > 0){
				CursorManager.removeBusyCursor();
			}
			return errorMessage;
		}
	}
}