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

public class ComboBoxLibraryPrepQCProtocol extends hci.flex.controls.ComboBox
{
    private var _parentApp:Object;

    public function ComboBoxLibraryPrepQCProtocol()
    {
        super();
        labelField="@display";
        valueField="@value";
        dataField="@idLibPrepQCProtocol";
        editable = false;
        isRequired=false;
        appendBlankRow = false;
        setDataProvider();
    }

    protected function setSelectedIndex():void {
        if ( this.data != null ) {
            this.selectedItem = this.getLibraryPrepQCProtocol(data);
        }
    }

    protected function getLibraryPrepQCProtocol(item:Object):Object {
        var id:String = item.@idLibPrepQCProtocol;
        var types:XMLList = parentApp.dictionaryManager.getEntry("hci.gnomex.model.LibraryPrepQCProtocol", id);
        if (types.length() == 1) {
            return types[0];
        } else {
            return null;
        }
    }

    protected function setDataProvider():void {
        dataProvider = new XMLListCollection();
        var dp:XMLListCollection = XMLListCollection(dataProvider);
        var idLibPrepQCProtocol:String = "";
//        if (data != null) {
//            idLibPrepQCProtocol = data.@idLibPrepQCProtocol;
//        }

        if (parentApp != null){

            this.enabled = true;

            var types:XMLListCollection= new XMLListCollection(XMLList(parentApp.dictionaryManager.xml.Dictionary.(@className == 'hci.gnomex.model.LibraryPrepQCProtocol').DictionaryEntry.(@value != '')).copy());
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

    override protected function focusOutHandler(event:FocusEvent):void
    {
        super.focusOutHandler(event);
        if( this.selectedItem!=null ){
            data.@idLibPrepQCProtocol = this.selectedItem.@idLibPrepQCProtocol;
            this.setSelectedIndex();
        }  else {
            data.@idLibPrepQCProtocol = '';
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
                                      updateData:Boolean = false):IFactory {
        return new ComboBoxLibraryPrepQCProtocolFactory({parentApp: parentApp,
            updateData: updateData});
    }
}
}

import mx.core.IFactory;

import views.renderers.ComboBoxLibraryPrepQCProtocol;

class ComboBoxLibraryPrepQCProtocolFactory implements mx.core.IFactory {
    private var properties:Object;

    public function ComboBoxLibraryPrepQCProtocolFactory(properties:Object) {
        this.properties = properties;
    }

    public function newInstance():* {
        var cb:views.renderers.ComboBoxLibraryPrepQCProtocol = new views.renderers.ComboBoxLibraryPrepQCProtocol();

        cb.parentApp = properties.parentApp;
        cb.updateData = properties.updateData;

        return cb;
    }
}