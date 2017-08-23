/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Component,OnInit} from "@angular/core";
import {FormGroup,FormBuilder,Validators } from "@angular/forms"
import {PrimaryTab} from "../../util/tabs/primary-tab.component"


@Component({
    selector: "test",
    template: `
 <form class="form-horizontal" (ngSubmit)="save()" [formGroup]="testForm">
    <fieldset>
        <div class="form-group" [ngClass]="{'has-error': (testForm.get('energyDrink').touched || testForm.get('energyDrink').dirty) && !testForm.get('energyDrink').valid }">
            <label class="col-md-2 control-label" for="energyNameId">Energy Drink </label>

            <div class="col-md-8">
                <input class="form-control" id="energyNameId" type="text" placeholder="Energy Drink (required)" formControlName="energyDrink"
                />
                <span style="color:red;" class="help-block" *ngIf="(testForm.get('energyDrink').touched || testForm.get('energyDrink').dirty) && testForm.get('energyDrink').errors">
                            <span *ngIf="testForm.get('energyDrink').errors.required">
                                Please enter your Energy Drink name.
                            </span>
                <span style="color:red;" *ngIf="testForm.get('energyDrink').errors.minlength">
                                The energy drink name must be longer than 3 characters.
                            </span>
                </span>
            </div>
        </div>
        <div class="form-group">

            <button type="submit" [disabled]="!testForm.valid"> Test Push </button>
        </div>

    </fieldset>
</form>
<br>Dirty: {{ testForm.dirty }}
<br>Touched: {{ testForm.touched }}
<br>Valid: {{ testForm.valid }}
<br>Value: {{ testForm.value | json }}
<br>Parent Valid: {{ this.theForm.valid | json}}
   
`
})
export class TestComponent extends PrimaryTab implements OnInit{
    testForm:FormGroup;
    name: string = "Test Tab";

    constructor(fb:FormBuilder){
        super(fb);

    }

    ngOnInit(){
        this.testForm = this.fb.group({
            energyDrink: ['', [Validators.required, Validators.minLength(3)]],
        });
        this.addChildToForm(this.testForm);
    }
    save(){
        this.changeStatus.emit({status:true, component: this});
    }
}
