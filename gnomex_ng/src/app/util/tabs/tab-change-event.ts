export interface TabChangeEvent {
    /**
     * Id of the currently active tab
     */
    activeTabId: number;

    /**
     * Id of the newly selected tab
     */
    nextId: number;

    /**
     * Function that will prevent tab switch if called
     */
    preventDefault: () => void;
}