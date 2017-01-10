package views.renderers
{
import flash.events.FocusEvent;

import hci.flex.renderers.RendererFactory;
import hci.flex.controls.ComboBox;

import mx.collections.HierarchicalCollectionView;
import mx.collections.IList;
import mx.collections.Sort;
import mx.collections.XMLListCollection;
import mx.controls.AdvancedDataGrid;
import mx.controls.Alert;
import mx.controls.ComboBox;
import mx.controls.DataGrid;
import mx.core.IFactory;
import mx.events.CollectionEvent;
import mx.events.ListEvent;

public class ComboBoxPipelineProtocol extends hci.flex.controls.ComboBox
{
    private var _parentApp:Object;
    private var _idCoreFacility:String;

    public function ComboBoxPipelineProtocol()
    {
        super();
        labelField="@display";
        valueField="@value";
        dataField="@idPipelineProtocol";
        editable = false;
        isRequired=false;
        appendBlankRow = true;
        setDataProvider();
    }

    protected function setSelectedIndex():void {
        if ( this.data != null ) {
            this.selectedItem = this.getPipelineProtocol(data);
        }
    }

    protected function getPipelineProtocol(item:Object):Object {
        var id:String = item.@idPipelineProtocol;
        var types:XMLList = parentApp.dictionaryManager.getEntry("hci.gnomex.model.PipelineProtocol", id);
        if (types.length() == 1) {
            return types[0];
        } else {
            return null;
        }
    }

    protected function setDataProvider():void {
        dataProvider = new XMLListCollection();
        var dp:XMLListCollection = XMLListCollection(dataProvider);
        if (parentApp != null){
            this.enabled = true;
            var types:XMLListCollection= new XMLListCollection(XMLList(parentApp.dictionaryManager.xml.Dictionary.(@className == 'hci.gnomex.model.PipelineProtocol').DictionaryEntry.(@value != '' && @idCoreFacility == idCoreFacility)).copy());
            dp.addAll(types);
        } else {
            this.enabled = false;
            selectedItem = null;
        }
        if ( dp.length == 0 ) {
            this.enabled = false;
            selectedItem = null;
        } else {
            setSelectedIndex();
        }
    }

    public function get parentApp():Object
    {
        return _parentApp;
    }

    public function set parentApp(value:Object):void
    {
        _parentApp = value;
    }

    public function get idCoreFacility():String
    {
        return _idCoreFacility;
    }

    public function set idCoreFacility(value:String):void
    {
        _idCoreFacility = value;
    }

    override protected function focusOutHandler(event:FocusEvent):void
    {
        super.focusOutHandler(event);
        if( this.selectedItem!=null ){
            data.@idPipelineProtocol = this.selectedItem.@idPipelineProtocol;
            this.setSelectedIndex();
        }  else {
            data.@idPipelineProtocol = '';
        }
    }
    override protected function focusInHandler(event:FocusEvent):void
    {
        this.setSelectedIndex();
    }

    override public function set data(value:Object):void {
        super.data = value;
        setDataProvider();
    }

    public static function getFactory(parentApp:Object,
                                      idCoreFacility:String,
                                      updateData:Boolean = false):IFactory {
        return new ComboBoxPipelineProtocolFactory({
            parentApp: parentApp,
            idCoreFacility: idCoreFacility,
            updateData: updateData});
    }
}
}

import mx.core.IFactory;

import views.renderers.ComboBoxPipelineProtocol;

class ComboBoxPipelineProtocolFactory implements mx.core.IFactory {
    private var properties:Object;

    public function ComboBoxPipelineProtocolFactory(properties:Object) {
        this.properties = properties;
    }

    public function newInstance():* {
        var cb:views.renderers.ComboBoxPipelineProtocol = new views.renderers.ComboBoxPipelineProtocol();

        cb.parentApp = properties.parentApp;
        cb.idCoreFacility = properties.idCoreFacility;
        cb.updateData = properties.updateData;

        return cb;
    }
}