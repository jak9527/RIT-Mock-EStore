import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-countdown-timer',
  templateUrl: './countdown-timer.component.html',
  styleUrls: ['./countdown-timer.component.css']
})
export class CountdownTimerComponent implements OnInit, OnDestroy {
  @Input() targetYear: number = new Date().getFullYear();
  @Input() targetMonth: number = new Date().getMonth() + 1; // Months are zero-based in TypeScript
  @Input() targetDay: number = new Date().getDate();
  @Input() targetHours: number = 0;
  @Input() targetMinutes: number = 0;
  @Input() targetSeconds: number = 0;

  remainingTime: string = '';
  private timerSubscription: Subscription | null = null;

  ngOnInit(): void {
    const targetDate = new Date(this.targetYear, this.targetMonth - 1, this.targetDay, this.targetHours, this.targetMinutes, this.targetSeconds);
    this.remainingTime = this.formatTime(this.getRemainingTime(targetDate));

    const timer$ = interval(1000);

    this.timerSubscription = timer$.subscribe(() => {
      this.remainingTime = this.formatTime(this.getRemainingTime(targetDate));

      if (this.getRemainingTime(targetDate) <= 0) {
        this.timerSubscription?.unsubscribe();
      }
    });
  }

  ngOnDestroy(): void {
    this.timerSubscription?.unsubscribe();
  }

  private getRemainingTime(targetDate: Date): number {
    const now = new Date();
    const remainingMilliseconds = targetDate.getTime() - now.getTime();
    return Math.floor(remainingMilliseconds / 1000);
  }

  private formatTime(seconds: number): string {
    if (seconds <= 0) {
      return '00:00:00';
    }

    const hours = Math.floor(seconds / 3600);
    const remainingMinutes = Math.floor((seconds % 3600) / 60);
    const remainingSeconds = seconds % 60;
    return `${hours.toString().padStart(2, '0')}:${remainingMinutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`;
  }
}
