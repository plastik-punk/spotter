import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NavigationStateService {
  private state: any = {};

  setNavigationState(state: any) {
    this.state = state;
  }

  getNavigationState() {
    const currentState = { ...this.state };
    this.clearNavigationState();
    return currentState;
  }

  private clearNavigationState() {
    this.state = {};
  }
}
