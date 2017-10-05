declare module '@woodpigeon/b3-data' {

    export interface IData {
        commit(data: Uint8Array): Promise<void>;
        flush(): Promise<void>;
    }

    export class Data implements IData {
        constructor();
        commit(data: Uint8Array): Promise<void>;
        flush(): Promise<void>;
    }

}
