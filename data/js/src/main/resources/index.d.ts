
declare module '@woodpigeon/b3-data' {

    export type EventData = {  }
    export type EventOffset = Number

    export type RefMap<P> = { [ref:string]: P }

    export type EventReader = (ranges: RefMap<EventOffset>) => Promise<RefMap<EventData>>
    export type EventWriter = (events: RefMap<EventData>) => Promise<Boolean>

    export interface IEventLog { }

    export class AdHocEventLog implements IEventLog {
        constructor(read: EventReader, write: EventWriter);
    }

    export class InMemoryEventLog implements IEventLog {
    }

    export class Fons {
        constructor(eventLog: IEventLog)
        view(streamRef: String, aggrType: String): Promise<Int8Array>;
    }

    export class Sink {
        constructor(eventLog: IEventLog);
        commit(data: Int8Array): Promise<void>;
        flush(): Promise<void>;
    }

}
