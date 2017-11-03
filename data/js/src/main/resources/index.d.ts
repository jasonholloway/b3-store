
declare module '@woodpigeon/b3-data' {


    export type EventData = {  }
    export type EventOffset = Number

    export type RefMap<P> = { [ref:string]: P }

    export type EventReader = (ranges: RefMap<EventOffset>) => Promise<RefMap<EventData>>
    export type EventWriter = (events: RefMap<EventData>) => Promise<Boolean>


    export class EventLog {
        constructor(read: EventReader, write: EventWriter);
    }

    export class Fons {
        constructor(eventLog: EventLog)
        view(id: String): Any;
    }

    export interface ISink {
        commit(data: Uint8Array): Promise<void>;
        flush(): Promise<void>;
    }

    export class Sink implements ISink {
        constructor(eventLog: EventLog);
        commit(data: Uint8Array): Promise<void>;
        flush(): Promise<void>;
    }

}
