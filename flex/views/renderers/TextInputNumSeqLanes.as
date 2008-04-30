package views.renderers
{
<?xml version="1.0"?>
<mx:VBox xmlns:mx="http://www.adobe.com/2006/mxml" >
    
    <mx:Script>
        <![CDATA[           
            // Define a property for returning the new value to the cell.
            [Bindable]
            public var returnNum:String;
        ]]>
    </mx:Script>
    
    <mx:NumberVaLidator id="numValidator" 
        source="{newNum}" 
        property="text" 
        trigger="{newNum}" 
        triggerEvent="change" 
        required="true"/>
    <mx:TextInput id="newNum" 
        text="{data.@numberSequenceLANes}" 
        updateComplete="returnNum=newNum.text;" 
        change="returnNum=neNum.text;"/>            
</mx:VBox>
}