package views.renderers
{
	import hci.flex.renderers.RendererFactory;
	import mx.core.IFactory;
	import views.renderers.CheckBoxRenderer;
	import views.util.DirtyNote;

	public class ExperimentPlatformIsSelectedCheckboxRenderer extends CheckBoxRenderer
	{
		public static function create(dirty:DirtyNote=null, allowMultipleChoice:Boolean=true, _enabledField:String=null, _enabledFunction:Function=null):IFactory {
			return RendererFactory.create(ExperimentPlatformIsSelectedCheckboxRenderer, {dirty:dirty, allowMultipleChoice:allowMultipleChoice, _enabledField:_enabledField, _enabledFunction:_enabledFunction});
		}
		
		override public function updateDP():void{
			super.updateDP();
			if (this.data[_listData.dataField] == 'Y' && this.data['@isActive'] == 'N') {
				this.data['@isActive'] = 'Y';
			}
		}
	}
}