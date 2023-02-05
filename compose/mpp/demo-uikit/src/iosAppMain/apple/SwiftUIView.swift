/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import SwiftUI
import UIKit

struct SwiftUIView: View {

    @State private var textState: String = "text state"
    @State private var interactResult: InteractResult = InteractResult.init(Color.teal, false)

    private let openCompose: () -> Void

    public init(openCompose: @escaping () -> Void) {
        self.openCompose = openCompose
//        UITabBar.appearance().backgroundColor = .gray
    }

    let gradient = LinearGradient(colors: [.blue.opacity(1.0), .green.opacity(1.0)],
            startPoint: .topLeading,
            endPoint: .bottomTrailing)

    var body: some View {
        TabView {

            ZStack {
                NavigationView {
                    VStack {
                        UIKitViewControllerToSwiftUI { result in
                            interactResult = result
                        }
                                .position(x: UIScreen.main.bounds.width / 2, y: UIScreen.main.bounds.height / 2)

                        HStack {
                            TextField("Type message...", text: $textState, axis: .vertical)
                                    .lineLimit(3)
                                    .background(Color.pink.opacity(0.0), in: RoundedRectangle(cornerRadius: 10))
//                                    .textFieldStyle(.roundedBorder)
                            Button(action: {}) {
                                HStack {
                                    Image(systemName: "play.fill")
                                    Text("Send")
                                }
                            }
                        }.padding(10)
                                .background(RoundedRectangle(cornerRadius: 10).fill(interactResult.swiftUIColor.opacity(0.7)))
                                .padding(6)

                        Rectangle()
                                .fill(Color.clear)
                                .frame(height: 10)
                                .background(interactResult.swiftUIColor)
                    }
                            .navigationBarTitleDisplayMode(.inline)
                            .navigationTitle("Compose inside SwiftUI")
                            .toolbarBackground(interactResult.swiftUIColor, for: .navigationBar)
                            .toolbarBackground(.visible, for: .navigationBar)
                            .statusBar(hidden: false)
                }.frame(maxHeight: .infinity)

            }
                        .tabItem {
                            Label("Compose", systemImage: "square.and.pencil")
                        }
                        .toolbar(.visible, for: .tabBar)
//                        .toolbarBackground(interactResult.swiftUIColor, for: .tabBar)
//                        .toolbarBackground(.visible, for: .tabBar)
                        .preferredColorScheme(interactResult.darkTheme ? .dark : .light)


                NavigationView {
                    VStack {
                        Text("Page 2")
                    }
                            .navigationBarTitleDisplayMode(.inline)
                            .navigationTitle("SwiftUI")
                }
                        .tabItem {
                            Label("SwiftUI", systemImage: "list.dash")
                        }
                        .toolbar(.visible, for: .tabBar)
//                        .toolbarBackground(interactResult.swiftUIColor, for: .tabBar)
//                        .toolbarBackground(.visible, for: .tabBar)
                        .preferredColorScheme(interactResult.darkTheme ? .dark : .light)


        }
    }

//    var body: some View {
//        VStack {
//            Text("SwiftUI text")
//            Button(action: {
//                openCompose()
//            }, label: {
//                Text("Open Compose screen")
//            })
////            UIKitToSwiftUI()
//            UIKitViewControllerToSwiftUI {
//                print("do nothing")
//            }
////            Spacer()

//
//        }
//    }
}
