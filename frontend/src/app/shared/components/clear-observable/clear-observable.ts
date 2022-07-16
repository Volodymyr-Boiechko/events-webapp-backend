import {Component, OnDestroy} from "@angular/core";
import {Subject} from "rxjs";

@Component({
  template: ''
})
export class ClearObservable implements OnDestroy {

  destroy$: Subject<boolean> = new Subject();

  constructor() {
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }

}
