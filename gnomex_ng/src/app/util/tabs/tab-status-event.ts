
export interface TabsStatusEvent {
    //disableProceedingTabs: () => void
    currentStatus: boolean;
    statusOfTabs: (status:boolean,start?:number, end?:number) => void
    tabId:number;
    tabLength:number;

    //tabAction?:any;
}