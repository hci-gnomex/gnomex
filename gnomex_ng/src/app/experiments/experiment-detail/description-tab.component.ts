/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Component,OnInit} from "@angular/core";
import {FormGroup,FormBuilder,Validators } from "@angular/forms"
import {PrimaryTab} from "../../util/tabs/primary-tab.component"


@Component({
    selector: "description",
    templateUrl: "./description-tab.component.html",
    styles:[`textarea { resize: none; } `]

})
export class DescriptionTab extends PrimaryTab implements OnInit{
    public static readonly tbarSettings:string  ="bold italic underline | left center right"
    private toolbarSettings:string
    descriptionForm:FormGroup;
    name: string = "Description Tab";
    //@Override
    protected _state:string;

    constructor(fb:FormBuilder){
        super(fb);

    }



    //@Override
    public setState(value: string) {
        /* this.tabIsActive should be checked like below so you know the component is visible when state changes.
            If you change state while the tab is invisible jqwidgets won't be able to redraw itself properly.
            If you don't do this check it will not break anything it's just not a neccessary expense
            Erik
         */
        this._state = value;
        super.setState(this._state);
        if(this.tabIsActive){ 
            if(this.edit){
                this.toolbarSettings = DescriptionTab.tbarSettings;
            }
            else{
                this.toolbarSettings = '';
            }

        }

    }
    //@Override
    public getState(){
        return this._state
    }





    ngOnInit(){
        this.descriptionForm = this.fb.group({
            expName: '',
            expDescript: ['',[Validators.maxLength(5000)]],
            notesForCore:['',[Validators.maxLength(2000)]],
        });
        this.addChildToForm(this.descriptionForm);
    }

    save(){
        this.changeStatus.emit({status:true, component: this});
    }

}
