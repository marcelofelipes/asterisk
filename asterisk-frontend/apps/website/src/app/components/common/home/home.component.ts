import {Component, OnInit} from '@angular/core';
import * as SockJS from 'sockjs-client';
import {CompatClient, IMessage, Stomp} from "@stomp/stompjs";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {environment} from "../../../../environments/environment";


@Component({
  selector: 'asterisk-frontend-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {

  public readonly messageForm: FormGroup = new FormGroup({
    message: new FormControl('', [Validators.required]),
  });

  public message = "";

  topic = "/topic/greetings";
  client: CompatClient | undefined;

  ngOnInit(): void {
    const sock = new SockJS(environment.backendUrl + '/ws');
    this.client = Stomp.over(sock);
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    const _this: this = this;
    _this.client?.connect({}, () => {
     _this.client?.subscribe(this.topic, (message => _this.handleMessage(message)))
    })
  }

  public sendMessage(): void {
    this.client?.send("/app/hello", {}, this.messageForm.get('message')?.value);
    this.messageForm.setValue({message: ''});
  }

  private handleMessage(message: IMessage): void {
    this.message = message.body;
  }
}
