package views.renderers
{
	import hci.flex.renderers.RendererFactory;
	
	import mx.core.IFactory;
	
	import views.renderers.CheckBoxRenderer;
	import views.util.DirtyNote;

	public class ExperimentPlatformIsSelectedCheckboxRenderer extends CheckBoxRenderer
	{
		
		[Bindable]
		public var _codeRequestCategory:String;

		public static function create(dirty:DirtyNote=null, allowMultipleChoice:Boolean=true, _enabledField:String=null, _enabledFunction:Function=null, _codeRequestCategory:String=null):IFactory {
			return RendererFactory.create(ExperimentPlatformIsSelectedCheckboxRenderer, {dirty:dirty, allowMultipleChoice:allowMultipleChoice, _enabledField:_enabledField, _enabledFunction:_enabledFunction, _codeRequestCategory:_codeRequestCategory});
		}
		
		override public function updateDP():void{
			super.updateDP();
			if (this.data[_listData.dataField] == 'Y' && this.data['@isActive'] == 'N') {
				this.data['@isActive'] = 'Y';
			}
			if (_codeRequestCategory != null) {
				for each (var rca:Object in this.data.RequestCategoryApplication) {
					if (rca.@value == _codeRequestCategory) {
						rca.@isSelected = this.data[_listData.dataField];
						break;
					}
				}
			}
		}
	}
}