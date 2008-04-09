package views
{
    
    import flash.display.Loader;
    import flash.display.LoaderInfo;
    import flash.display.DisplayObject;
    import flash.display.Sprite;
    import flash.display.Bitmap;
    import flash.net.URLRequest;
    import flash.events.Event;
    import mx.controls.Text;
    
    import mx.core.UIComponent;    
    import mx.core.UITextField;
    import mx.controls.DataGrid;    
    import mx.controls.listClasses.IListItemRenderer;
    
    public class DataGridSampleDragProxy extends UIComponent {
        
        public function DataGridSampleDragProxy():void
        {
            super();
        }
        
        override protected function createChildren():void
        {
            super.createChildren();
            
            //retrieve the selected indicies and then sort them
            //in order to display them in the proper order
            var items:Array = mx.controls.DataGrid(owner).selectedIndices;
            items.sort();
            
            var len:int = items.length;
            
            for (var i:int=0;i<len;i++)
            {
                var dg:mx.controls.DataGrid = mx.controls.DataGrid(owner);
                
                var item:Object = dg.dataProvider[items[i]];
                
                var label:UITextField = new UITextField();
                label.text = item.@name;
                label.width=200;
                label.y = 30;

                addChild(label);
            }
            
            if (len == 1)
            {
                var src:IListItemRenderer = dg.indexToItemRenderer(items[0]);
                //y = src.y - 20;
            }
            
            //x = this.mouseX - (labelWidth * 6);
            //x = this.mouseX - 120;

        }

    }
}
