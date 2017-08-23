import { Component, EventEmitter, Output, Input } from '@angular/core'
import { FormGroup, FormBuilder, FormArray, Validators, AbstractControl } from '@angular/forms'
import {ComponentCommunicatorEvent} from './component-status-event'
import {TabContainer} from "./tab-container.component";

import 'rxjs/add/operator/debounceTime'

@Component({

    selector: 'prep-tab',
    template: `
              <div> </div>
    `
})
export class PrimaryTab {
    name: string
    @Output() changeStatus = new EventEmitter<ComponentCommunicatorEvent>();
    private _theForm: FormGroup;
    protected _state:string;
    protected edit:boolean;
    protected _tabVisible:boolean ;

    constructor(protected fb: FormBuilder) {
        this.name = "A Nameless Tab";
    }

    public set tabIsActive(visible:boolean){
        this._tabVisible = visible;
    }
    public get tabIsActive():boolean{
        return this._tabVisible;
    }

    public set theForm(value: FormGroup) {
        this._theForm = value;
    }
    public get theForm() {
        return this._theForm;
    }

    public setState(value: string) {
        this._state = value;
        if(this._state === TabContainer.NEW || this._state === TabContainer.EDIT) {
            this.edit = true;
        }
        else if(this._state === TabContainer.VIEW){
            this.edit = false
        }

    }
    public getState() {
        return this._state;
    }

    protected addChildToForm(childForm:FormGroup):void{
        const childForms = (<FormArray>this.theForm.root).controls["childForms"];
        childForms.push(childForm);
    }

    protected validateControl(value:any,address:string){
        const childForms = (<FormArray>this.theForm.root).controls["childForms"];
        let control: AbstractControl
        for(let i = 0; i < childForms.length; i++){
            control= childForms.at(i).get(address);
            if(control && !!control.validator){
                let message:string = "loook here chippy";
                console.log(control.errors);
                control.setErrors({'required': true})
                //control.setValue("<div>${message}</div>");
                break;
            }
        }
        console.log(control)

    }
    protected controlsToLink(address:string, control: AbstractControl):void{
        control.valueChanges.debounceTime(2000).subscribe(value => this.validateControl(value,address))
    }

}