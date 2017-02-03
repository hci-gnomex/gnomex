/**
 * Created by John Hofer on 2/3/2017.
 */
package hci.flex.util {

import flash.events.Event;
import flash.events.KeyboardEvent;
import flash.events.TextEvent;
import flash.ui.Keyboard;

import mx.controls.RichTextEditor;
import mx.controls.textClasses.TextRange;
import mx.core.UIComponent;
import mx.events.FocusRequestDirection;

public class HCIRichTextEditor extends RichTextEditor{

    private var pastedDescription:String = "";
    private var descriptionChangeIsPaste:Boolean = false;
    private var simpleLockForTab:Boolean = false;

    private var firstTime:Boolean = true;

    public function HCIRichTextEditor() {
        super();
    }

    override protected function commitProperties():void {
        super.commitProperties();

        if (firstTime) {
            this.addEventListener(TextEvent.TEXT_INPUT, onDescriptionTextInput);
            this.addEventListener(KeyboardEvent.KEY_DOWN, onTabPressed);
            this.addEventListener(Event.CHANGE, onChange);
        }
    }

    /**
     * This function is intended to allow the use of the TAB key to insert "\t" characters into the project
     * description. Unfortunately, the RichTextEditor component does not want to allow these characters, so we need
     * to improvise.
     *
     * @param event an event signalling that a key was pressed. If it is the TAB key, we do stuff, otherwise,
     *     nothing.
     */
    private function onTabPressed(event:KeyboardEvent):void {
        switch (event.keyCode) {
            case Keyboard.TAB :
                // super-simple lock on character insertion (the focus movements interact somewhat oddly otherwise)
                if( this.simpleLockForTab ) {
                    focusManager.moveFocus(FocusRequestDirection.BACKWARD);
                    break;
                }
                simpleLockForTab = true;

                var indexBegin:int = event.currentTarget.textArea.selectionBeginIndex;
                var indexEnd:int = event.currentTarget.textArea.selectionEndIndex;

                var range:TextRange = new TextRange(event.currentTarget.textArea
                                                    as UIComponent, false, indexBegin, indexEnd);
                range.text = "\t";

                var indexNext:int = event.currentTarget.textArea.selectionBeginIndex + range.text.length;
                event.currentTarget.textArea.setSelection(indexNext, indexNext);
                focusManager.moveFocus(FocusRequestDirection.BACKWARD);

                this.simpleLockForTab = false;
                break;
        }
    }

    /**
     * This function is supposed to listen for any paste events on the textArea. If one is detected,
     * then it should call our replacement for the paste operation.
     *
     * This detection may be version-dependent.
     *
     * This is intended to take effect in a specific order, but requires event-driven timing.
     * First, on a paste command, a textInput event fires, we add the correctly-formatted text and save the result.
     * Second, outside of our influence, the textArea pastes the text in its (possibly incorrect) way
     * Finally, this triggers a change event, at which point we overwrite the data with our change from step 1.
     *
     * @param event an event signalling a potential paste action has been taken.
     *
     * @version flex_sdk_3.6
     * @author John Hofer
     * @since 02/03/2017
     */
    private function onDescriptionTextInput(event:TextEvent):void {
        this.descriptionChangeIsPaste = false;

        // So it turns out that typing typically does fire a TextEvent.TextInput event, but not the TAB key.
        // This means that the ONLY changes we pick out here are paste events that (should) contain TABs.
        // It will also pick up the pasting of a singular TAB character.
        if(event.text.indexOf("\t") >= 0) {
            onDescriptionPaste(event);
            this.descriptionChangeIsPaste = true;
        }
    }

    /**
     * This function is designed to duplicate the effect of a copy operation on this component's RichTextArea's
     * textArea, but to have it actually keep characters like "\t".
     *
     * This is intended to take effect in a specific order, but requires event-driven timing.
     * First, on a paste command, a textInput event fires, we add the correctly-formatted text and save the result.
     * Second, outside of our influence, the textArea pastes the text in its (possibly incorrect) way
     * Finally, this triggers a change event, at which point we overwrite the data with our change from step 1.
     *
     * @param event an event signalling a potential copy action has been taken.
     *
     * @author John Hofer
     * @since 02/03/2017
     */
    private function onDescriptionPaste(event:TextEvent):void {
        var indexBegin:int = event.currentTarget.selectionBeginIndex;
        var indexEnd:int = event.currentTarget.selectionEndIndex;

        var range:TextRange = new TextRange(event.currentTarget as UIComponent, false, indexBegin, indexEnd);
        range.text = event.text;

        var indexNext:int = indexBegin + range.text.length;
        event.currentTarget.setSelection(indexNext, indexNext);

        this.pastedDescription = this.textArea.text;
    }

    /**
     * This function is built to add in the correctly-formatted data into the description, as the paste command for
     * RichTextEditors oddly excludes certain symbols (such as '\t' characters from JIRA GNOM-2154).
     *
     * This is intended to take effect in a specific order, but requires event-driven timing.
     * First, on a paste command, a textInput event fires, we add the correctly-formatted text and save the result.
     * Second, outside of our influence, the textArea pastes the text in its (possibly incorrect) way
     * Finally, this triggers a change event, at which point we overwrite the data with our change from step 1.
     *
     * @param event an event signalling a potential copy action has been taken.
     *
     * @author John Hofer
     * @since 02/03/2017
     */
    private function onChange(event:Event):void {
        // TextInput events always cause change events to trigger, so if there is a paste action, we act.
        if (this.descriptionChangeIsPaste) {
            this.textArea.text = this.pastedDescription;
            this.descriptionChangeIsPaste = false;
        }
    }
}
}
