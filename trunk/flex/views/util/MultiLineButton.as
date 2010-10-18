package views.util
{
    import flash.text.TextFieldAutoSize; 
    import mx.controls.Button; 
 
    public class MultiLineButton extends Button 
    { 
 
 
        public function  MultiLineButton() 
        { 
                super(); 
        } 
 
        override protected function createChildren():void 
        { 
                super.createChildren(); 
 
                textField.multiline = true; 
                textField.wordWrap = true; 
                textField.autoSize = TextFieldAutoSize.CENTER; 
        } 
 
        override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void 
        { 
                super.updateDisplayList(unscaledWidth, unscaledHeight); 
                textField.y = (this.height - textField.height) >> 1; 
 
                //height = textField.height + getStyle("paddingTop") + getStyle("paddingBottom"); 
        } 
    } 
} 
