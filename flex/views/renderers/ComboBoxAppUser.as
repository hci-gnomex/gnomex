/**
 * Created by u0395021 on 7/29/2016.
 */
package views.renderers {
import hci.flex.renderers.RendererFactory;
import mx.core.IFactory;


public class ComboBoxAppUser extends views.renderers.ComboBox {
    public static function create(dataField:String):IFactory {
        return RendererFactory.create(ComboBoxAppUser,
                { dataField: dataField,
                    updateData: true,
                    valueField: '@idAppUser',
                    labelField: '@display'});
    }

    override protected function initializationComplete():void
    {
        dataProvider = parentDocument.coreAdmins;
        super.initializationComplete();
    }
}
}
