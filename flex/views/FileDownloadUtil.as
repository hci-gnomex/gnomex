package views
{
	public class FileDownloadUtil
	{
		
		import mx.controls.Button;
		import mx.controls.ProgressBar;
		import flash.net.URLRequest;
    	import flash.net.URLRequestMethod;
    	import flash.net.URLVariables;
    	import flash.events.Event;
    	import flash.events.ProgressEvent;
    	import mx.managers.PopUpManager;
    	import flash.net.FileReference;
		
		
	private var fr:FileReference;
    private var pb:ProgressBar;
    private var btn:Button;
    private var cancelButton:Button;
    private var url:String = "";
    private var params:Object;
    
    
    public function setURL(url:String, params:Object):void {
    	this.url = url;
    	this.params = params;
    }

    /**
     * Set references to the components, and add listeners for the OPEN, 
     * PROGRESS, and COMPLETE events.
     */
    public function init(pb:ProgressBar, btn:Button, cancelButton:Button):void
    {
        // Set up the references to the progress bar and cancel button,
        // which are passed from the calling script.
        this.pb = pb;
        this.btn = btn;
        this.cancelButton = cancelButton;

        fr = new FileReference();
        fr.addEventListener(Event.OPEN, openHandler);
        fr.addEventListener(ProgressEvent.PROGRESS, progressHandler);
        fr.addEventListener(Event.COMPLETE, completeHandler);
    }
    
     /**
     * Begin downloading the file specified in the DOWNLOAD_URL constant.
     */
    public function startDownload():void
    {
        var request:URLRequest = new URLRequest();
        request.url = this.url;
        request.data = this.params;
        fr.download(request, "gnomex_data.zip");

    }
    
    public  function formatDate(date:Date):String {
	    return date.getFullYear().toString() + 
            '-' + (date.getMonth()+1).toString() + '-' + date.getDate();
    }
    


   /**
     * When the OPEN event has dispatched, change the progress bar's label 
     * and enable the "Cancel" button, which allows the user to abort the 
     * download operation.
     */
    private function openHandler(event:Event):void
    {
        pb.label = "Downloading %3%%";
        if (btn != null) {
	        btn.enabled = true;
        }
    }
    /**
     * While the file is downloading, update the progress bar's status.
     */
    private function progressHandler(event:ProgressEvent):void
    {
		pb.setProgress(event.bytesLoaded, event.bytesTotal);
    }
    
      /**
     * Once the download has completed, change the progress bar's label one 
     * last time and disable the "Cancel" button since the download is 
     * already completed.
     */
    private function completeHandler(event:Event):void
    {
        pb.label = "Download complete";
        pb.setProgress(1, 1);
        if (btn != null) {
	        btn.enabled = false;
        }
    }
    
    /**
     * Cancel the current file download.
     */
    public function cancelDownload():void
    {
        fr.cancel();
        pb.label = "Download cancelled";
        if (btn != null) {
	        btn.enabled = false;        	
        }
    }
 }
    
}