/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.runon.trading.technical.analysis.pattern.star;

/**
 * 샛별형 저점에서 발생하는 패턴
 * 보통은 3개의 캔들일떄 샛별형이라 불리지만 실제 차트에선 가운데 캔들이 3개 잇아으로 구성되기도 한다
 * 첫번째 캔들이 음봉이고 카운데 캔들이 팽이형,도지형,망치형,역망치형 등 다양한 캔들이 올 수 있다
 * 세번째 캔들은 종가가 최소한 첫번째 음봉의 몸통 절반위에 형성되는 관통형이나 상승장악형 이어야 한다
 * 하락세의 저점에서 직전에 양,음봉으로 부딪히는 자리선상에 있는 샛별형이어야 상승반전 신호로써 어느정도 유효하고
 * 상승세의 조정구간의 끝 또는 보합세의 저ㅓㅁ에서 출현할 때도 유효하다.
 * 어느 경우에든 충분한 조정 시에만 진입해야 한다.
 * @author macle
 */
public class MorningStart {
}
