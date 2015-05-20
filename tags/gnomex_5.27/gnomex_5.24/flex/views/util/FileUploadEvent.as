package views.util
{
	import flash.events.Event;
	import flash.net.FileReference;
	
	public class FileUploadEvent extends Event
	{
		private var _file:FileReference = null;

		public function FileUploadEvent(file:FileReference)
		{
			_file = file;
			super(Event.ADDED);
		}

		public function get file():FileReference
		{
			return _file;
		}

	}
}