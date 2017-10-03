declare module '@woodpigeon/b3-data' {

    export class Data {
        constructor();
        commit(update: any): void;
        flush(): void;
    }

}
