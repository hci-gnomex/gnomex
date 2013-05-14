package views.util
{
	import flash.display.DisplayObject;
	
	import mx.controls.ComboBox;
	import mx.controls.List;
	import mx.core.ClassFactory;
	import mx.core.UIComponent;
	
	public class IconComboBox extends ComboBox
	{
		
		//create our own factory so we can set the properties before initialized    
		private var internalDropdownFactory:ClassFactory = new ClassFactory(List);
		
		//holds the icon image for display
		private var displayIconObject:Object;
		
		
		public function IconComboBox():void{
			super();
			
			//setup the properties on the factory before init so that
			//the drop down will gracefully adopt them.
			internalDropdownFactory.properties = { iconField:"",iconFunction:null };
			dropdownFactory = internalDropdownFactory;
		}
		
		/**
		 * store the icon field
		 **/
		private var _iconField:String="icon";
		[Bindable]
		public function set iconField(value:String):void{
			_iconField = value;
			internalDropdownFactory.properties = {iconField:value};
			
		}
		public function get iconField():String{
			return _iconField;
		}
		
		/**
		 * store the icon function
		 **/
		private var _iconFunction:Function;
		[Bindable]
		public function set iconFunction(value:Function):void{
			_iconFunction = value;
			internalDropdownFactory.properties = {iconFunction:value};
		}
		public function get iconFunction():Function{
			return _iconFunction;
		}
		
		
		/**
		 * when the index changes so should the icon 
		 **/
		override public function set selectedIndex(value:int):void
		{
			super.selectedIndex = value;
			
			if (value!=-1){ 
				showIcon();
			}
			
		}
		
		//set the icon to the selected item
		private function showIcon():void
		{
			
			var displayIcon:Class = itemToIcon(dataProvider[selectedIndex]);
			
			//remove the previous added object so that a new one can 
			//be created. I would love to find a way to recycle this 
			//displayIconObject??     
			if (getChildByName("displayIconObject"))
			{
				removeChild(getChildByName("displayIconObject"));
			}
			
			//if no icon then return
			if (!displayIcon)
			{
				//move the textinput to 0 as there is no icon            
				textInput.x=0;
				return;
			}
			
			//add and size the obejct
			displayIconObject = new displayIcon;
			displayIconObject.name="displayIconObject";
			addChild(DisplayObject(displayIconObject));
			
			// set the x based on corerradius
			DisplayObject(displayIconObject).x = getStyle("cornerRadius");
			
			//set the y pos of the icon based on height
			DisplayObject(displayIconObject).y = (height-DisplayObject(displayIconObject).height)/2;
			
			//move the textinput to make room for the icon            
			textInput.x=DisplayObject(displayIconObject).width+getStyle("cornerRadius");
			
			
		}
		
		/**
		 * make sure to take into account the icon width 
		 **/        
		override public function set measuredWidth(value:Number):void
		{
			super.measuredWidth = value + (DisplayObject(displayIconObject).width+getStyle("cornerRadius"));
		}
		
		
		/**
		 * grab the icon based on the data
		 **/
		public function itemToIcon(data:Object):Class
		{
			if (data == null)
				return null;
			
			if (iconFunction != null)
				return iconFunction(data);
			
			var iconClass:Class;
			var icon:*;
			
			if (data is XML)
			{
				try
				{
					if (data[iconField].length() != 0)
					{
						icon = String(data[iconField]);
						if (icon != null)
						{
							iconClass =
								Class(systemManager.getDefinitionByName(icon));
							if (iconClass)
								return iconClass;
							
							return document[icon];
						}
					}
				}
				catch(e:Error)
				{
				}
			}
				
			else if (data is Object)
			{
				try
				{
					if (data[iconField] != null)
					{
						if (data[iconField] is Class)
							return data[iconField];
						
						if (data[iconField] is String)
						{
							iconClass = Class(systemManager.getDefinitionByName(
								data[iconField]));
							if (iconClass)
								return iconClass;
							
							return document[data[iconField]];
						}
					}
				}
				catch(e:Error)
				{
				}
			}
			
			return null;
		}
		
		
	}
}