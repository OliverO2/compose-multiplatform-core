import UIKit
import shared

class ViewController: UIViewController {

    private let contactButton: UIButton = {
        let button = UIButton()
        button.setTitle("Go to Compose", for: .normal)
        button.setTitleColor(.black, for: .normal)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = UIColor.green
        setupViews()
        setContraints()
        navigationController?.navigationBar

        contactButton.addTarget(
                self,
                action: #selector(contactButtonTapped),
                for: .touchUpInside
        )
    }

    @objc private func contactButtonTapped() {
        let contactViewController = SwiftHelper().getViewController()//todo
        navigationController?.pushViewController(contactViewController, animated: true)
    }

    private func setupViews() {
        title = "CustomNavBar"
        view.addSubview(contactButton)
    }

    private func setContraints() {
        NSLayoutConstraint.activate([
            contactButton.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            contactButton.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            contactButton.heightAnchor.constraint(equalToConstant: 50),
            contactButton.widthAnchor.constraint(equalToConstant: 150)
        ])
    }
}

