
declare module '@woodpigeon/b3-data' {

    export interface IEventLog {

    }

    export class Fons {
        constructor(eventLog: IEventLog)
        view(id: String): Any;
    }

    export interface ISink {
        commit(data: Uint8Array): Promise<void>;
        flush(): Promise<void>;
    }

    export class Sink implements ISink {
        constructor(eventLog: IEventLog);
        commit(data: Uint8Array): Promise<void>;
        flush(): Promise<void>;
    }

}
