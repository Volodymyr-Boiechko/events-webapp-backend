import {Injectable} from "@angular/core";
import * as moment from 'moment';
import {ToasterConfigService} from "./toaster-config.service";

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  constructor(private toaster: ToasterConfigService) {
  }

  isPublicPage(): boolean {
    let page = window.location.pathname.split('/')[1].split('?')[0];
    return page === 'public';
  }

  generateUniqueId(): string {
    return '_' + Math.random().toString(36).substr(2, 9);
  }

  // DATE & TIME

  formatDate(date: string, format: string = 'MMMM D, LT') {
    return moment.parseZone(date).local().format(format);
  }


  getTimeFromSeconds(seconds: number): string {
    return moment().startOf('day').seconds(seconds).format('LT');
  }


  transformTimeToSeconds(time: string): number {
    return moment.duration(moment(time, 'LT').format('HH:mm')).asSeconds();
  }


  getLocaleTimeFromUtcDate(utcDate: Date | string | number): string {
    return moment.utc(utcDate).local().format('LTS');
  }


  getLocaleDateFromUtcDate(utcDate: Date | string | number, useFormat: boolean = true): string {
    if (useFormat) {
      return moment.utc(utcDate).local().format('LL');
    } else {
      return moment.utc(utcDate).local().format();
    }
  }

  defaultError() {
    this.toaster.error('The connection timed out. If this error persists please contact your technical support.');
  }

}
