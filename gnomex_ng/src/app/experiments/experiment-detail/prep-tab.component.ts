import { Component } from '@angular/core'
import { FormGroup, FormBuilder, FormArray, Validators, AbstractControl } from '@angular/forms';
import { CommonModule } from '@angular/common'
import { PrimaryTab } from '../../util/tabs/primary-tab.component'

import 'rxjs/add/operator/debounceTime'

@Component({

    selector: 'prep-tab',
    templateUrl: './prep-tab.component.html'
})
export class PrepTab extends PrimaryTab {

    entries = [];
    name: string = "Prep Tab";
    selectedEntry: { [key: string]: any } = {
        value: null,
        description: null
    };
    emailMessage: string;

    private validationMessages = {
        required: 'Please enter your email address',
        pattern: 'Please enter a valid email address'
    };

    prepForm: FormGroup;



    constructor(protected fb: FormBuilder) {
        super(fb);
    }




    ngOnInit() {

        console.log("I am in the child")
        this.entries = [
            {
                description: 'entry 1',
                value: 1
            },
            {
                description: 'entry 2',
                value: 2
            },
            {
                description: 'entry 3',
                value: 3
            },
            {
                description: 'entry 4',
                value: 4
            }
        ];


        this.prepForm = this.fb.group({
            labName: ['', [Validators.required, Validators.minLength(3)]],
            billingAcount: '',
            experimentType: this.fb.array([]),
            emailGroup: this.fb.group({
                email: ['', [Validators.required, Validators.pattern('[a-z0-9._%+-]+@[a-z0-9.-]+')]],
                confirmEmail: ['', Validators.required],
            }, { validator: emailMatcher }),
            phone: '',
            notification: 'email',

        });


        this.addChildToForm(this.prepForm);  // important for cross form validation
        this.controlsToLink("expDescript",this.prepForm.get("labName"));



        // select the first one
        if (this.entries) {
            this.onSelectionChange(this.entries[0]);
        }


        this.prepForm.get("notification").valueChanges
            .subscribe(value => this.setNotification(value));
        const emailControl = this.prepForm.get('emailGroup.email');
        emailControl.valueChanges.debounceTime(1000).subscribe(value => {
            //this.invalidTab.emit(this);
            this.setMessage(emailControl)
        });
    }
    onSelectionChange(entry) {
        // clone the object for immutability
        this.selectedEntry = Object.assign({}, this.selectedEntry, entry); // copying entry into selectedEntry
    }
    save() {
        console.log(this.theForm);
        this.changeStatus.emit({status:true,component:this});
    }

    setNotification(notifyVia: string): void { // used for radio button email and text option
        // Will change validation during runtime

        const phoneControl = this.prepForm.get('phone'); // getting access to the phoneControl
        if (notifyVia === 'text') {
            phoneControl
            phoneControl.setValidators(Validators.required); // setting validator during runtime
        } else {
            phoneControl.clearValidators();
        }
        phoneControl.updateValueAndValidity();
    }

    setMessage(c: AbstractControl): void {
        this.emailMessage = '';
        if ((c.touched || c.dirty) && c.errors) {
            this.emailMessage = Object.keys(c.errors).map(key =>
                this.validationMessages[key]).join(' ');
        }
    }



}

function emailMatcher(c: AbstractControl): { [key: string]: boolean } | null { // returns if its valid returns null or object if invalid defining the broken rule
    // we have key and value pair. key is a string, value is boolean
    let emailControl = c.get('email');
    let confirmControl = c.get('confirmEmail');
    if (emailControl.pristine || confirmControl.pristine) {
        return null;
    }
    if (emailControl.value === confirmControl.value) {
        return null;
    }
    return { 'match': true };// if we need to reference the validator in html we do ...errors.match,
}