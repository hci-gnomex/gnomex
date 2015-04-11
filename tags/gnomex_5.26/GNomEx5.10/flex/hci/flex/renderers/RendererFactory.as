package hci.flex.renderers
{
	import mx.core.IFactory;
	import mx.core.ClassFactory;
	
	public class RendererFactory {  
		public static var DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND:uint = 0xFFFFB9;
		public static var DEFAULT_MISSING_REQUIRED_FIELD_BORDER:uint = 0xffff00;
		public static var DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS:uint = 0;
		public static var DEFAULT_HIGHLIGHT_BACKGROUND:uint = 0xFFFF99;
		

		public static function create(renderer:Class, properties:Object):IFactory {  
			var factory:ClassFactory = new ClassFactory(renderer);   
			factory.properties = properties;  
			return factory;
		}            


	}
}